/**
 *
 */
package br.com.sevenheads.security;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.WebUtils;

public final class MemcachedRepository implements SecurityContextRepository {

	private static final Log LOGGER = LogFactory.getLog(MemcachedRepository.class);
    private final Object contextObject = SecurityContextHolder.createEmptyContext();
    private boolean allowSessionCreation = true;
    private boolean disableUrlRewriting = false;
    private final boolean isServlet3 = ClassUtils.hasMethod(ServletRequest.class, "startAsync");
    private String springSecurityContextKey = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	/* (non-Javadoc)
	 * @see org.springframework.security.web.context.SecurityContextRepository#loadContext(org.springframework.security.web.context.HttpRequestResponseHolder)
	 */
	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();
        HttpSession httpSession = request.getSession(false);

        String jSessionId = getJSessionID(request);
        SecurityContext context = readSecurityContextFromSession(httpSession, jSessionId);

        SaveToSessionResponseWrapper wrappedResponse = new SaveToSessionResponseWrapper(response, request, httpSession != null, context);
        requestResponseHolder.setResponse(wrappedResponse);

        if(isServlet3) {
            requestResponseHolder.setRequest(new Servlet3SaveToSessionRequestWrapper(request, wrappedResponse));
        }

        return context;
	}

	private String getJSessionID(HttpServletRequest request) {
		for(Cookie cookie : request.getCookies()) {
			if("JSESSIONID".equalsIgnoreCase(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	@Cacheable(value="defaultCache", key="'JSESSIONID-' + #jSessionId")
	private SecurityContext readSecurityContextFromSession(HttpSession httpSession, String jSessionId) {
		if(httpSession == null || !jSessionId.matches(".*[a-zA-Z].*")) {
			return SecurityContextHolder.createEmptyContext();
		}
        // Session exists, so try to obtain a context from it.
        Object contextFromSession = httpSession.getAttribute(springSecurityContextKey);

        // We now have the security context object from the session.
        if (contextFromSession == null || !(contextFromSession instanceof SecurityContext)) {
            return SecurityContextHolder.createEmptyContext();
        }

        // Everything OK. The only non-null return from this method.
        return (SecurityContext) contextFromSession;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.context.SecurityContextRepository#saveContext(org.springframework.security.core.context.SecurityContext, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		SaveToSessionResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, SaveToSessionResponseWrapper.class);
        if(responseWrapper == null) {
            throw new IllegalStateException("Cannot invoke saveContext on response " + response
            		+ ". You must use the HttpRequestResponseHolder.response after invoking loadContext");
        }

        // saveContext() might already be called by the response wrapper
        // if something in the chain called sendError() or sendRedirect(). This ensures we only call it
        // once per request.
        if (!responseWrapper.isContextSaved() ) {
            responseWrapper.saveContext(context);
        }
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.context.SecurityContextRepository#containsContext(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public boolean containsContext(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session == null) {
            return false;
        }

        return session.getAttribute(springSecurityContextKey) != null;
	}

	/**
	 * @param allowSessionCreation the allowSessionCreation to set
	 */
	public void setAllowSessionCreation(boolean allowSessionCreation) {
		this.allowSessionCreation = allowSessionCreation;
	}

	/**
	 * @param disableUrlRewriting the disableUrlRewriting to set
	 */
	public void setDisableUrlRewriting(boolean disableUrlRewriting) {
		this.disableUrlRewriting = disableUrlRewriting;
	}

	/**
	 * @param springSecurityContextKey the springSecurityContextKey to set
	 */
	public void setSpringSecurityContextKey(String springSecurityContextKey) {
		this.springSecurityContextKey = springSecurityContextKey;
	}

	private static class Servlet3SaveToSessionRequestWrapper extends HttpServletRequestWrapper {
        private final SaveContextOnUpdateOrErrorResponseWrapper response;

        public Servlet3SaveToSessionRequestWrapper(HttpServletRequest request,SaveContextOnUpdateOrErrorResponseWrapper response) {
            super(request);
            this.response = response;
        }

        @Override
        public AsyncContext startAsync() {
            response.disableSaveOnResponseCommitted();
            return super.startAsync();
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest,
                ServletResponse servletResponse) throws IllegalStateException {
            response.disableSaveOnResponseCommitted();
            return super.startAsync(servletRequest, servletResponse);
        }
    }

	/**
     * Wrapper that is applied to every request/response to update the <code>HttpSession<code> with
     * the <code>SecurityContext</code> when a <code>sendError()</code> or <code>sendRedirect</code>
     * happens. See SEC-398.
     * <p>
     * Stores the necessary state from the start of the request in order to make a decision about whether
     * the security context has changed before saving it.
     */
    public final class SaveToSessionResponseWrapper extends SaveContextOnUpdateOrErrorResponseWrapper {

        private final HttpServletRequest request;
        private final boolean httpSessionExistedAtStartOfRequest;
        private final SecurityContext contextBeforeExecution;
        private final Authentication authBeforeExecution;

        /**
         * Takes the parameters required to call <code>saveContext()</code> successfully in
         * addition to the request and the response object we are wrapping.
         *
         * @param request the request object (used to obtain the session, if one exists).
         * @param httpSessionExistedAtStartOfRequest indicates whether there was a session in place before the
         *        filter chain executed. If this is true, and the session is found to be null, this indicates that it was
         *        invalidated during the request and a new session will now be created.
         * @param context the context before the filter chain executed.
         *        The context will only be stored if it or its contents changed during the request.
         */
        SaveToSessionResponseWrapper(HttpServletResponse response, HttpServletRequest request,
                                                      boolean httpSessionExistedAtStartOfRequest,
                                                      SecurityContext context) {
            super(response, disableUrlRewriting);
            this.request = request;
            this.httpSessionExistedAtStartOfRequest = httpSessionExistedAtStartOfRequest;
            this.contextBeforeExecution = context;
            this.authBeforeExecution = context.getAuthentication();
        }

        /**
         * Stores the supplied security context in the session (if available) and if it has changed since it was
         * set at the start of the request. If the AuthenticationTrustResolver identifies the current user as
         * anonymous, then the context will not be stored.
         *
         * @param context the context object obtained from the SecurityContextHolder after the request has
         *        been processed by the filter chain. SecurityContextHolder.getContext() cannot be used to obtain
         *        the context as it has already been cleared by the time this method is called.
         *
         */
        @Override
        protected void saveContext(SecurityContext context) {
            final Authentication authentication = context.getAuthentication();
            HttpSession httpSession = request.getSession(false);

            // See SEC-776
            if (authentication == null || trustResolver.isAnonymous(authentication)) {
                LOGGER.debug("SecurityContext is empty or contents are anonymous - context will not be stored in HttpSession.");

                if (httpSession != null && !contextObject.equals(contextBeforeExecution)) {
                    // SEC-1587 A non-anonymous context may still be in the session
                    // SEC-1735 remove if the contextBeforeExecution was not anonymous
                    httpSession.removeAttribute(springSecurityContextKey);
                }
                return;
            }

            if(httpSession == null) {
                httpSession = createNewSessionIfAllowed(context);
            }

            // If HttpSession exists, store current SecurityContext but only if it has
            // actually changed in this thread (see SEC-37, SEC-1307, SEC-1528)
            if (httpSession != null) {
                // We may have a new session, so check also whether the context attribute is set SEC-1561
                if (contextChanged(context) || httpSession.getAttribute(springSecurityContextKey) == null) {
                    httpSession.setAttribute(springSecurityContextKey, context);
                    LOGGER.debug("SecurityContext stored to HttpSession: '" + context + "'");
                }
            }
        }

        private boolean contextChanged(SecurityContext context) {
            return context != contextBeforeExecution || context.getAuthentication() != authBeforeExecution;
        }

        private HttpSession createNewSessionIfAllowed(SecurityContext context) {
            if(httpSessionExistedAtStartOfRequest) {
                LOGGER.debug("HttpSession is now null, but was not null at start of request; "
                        + "session was invalidated, so do not create a new session");
                return null;
            }

            if(!allowSessionCreation) {
                LOGGER.debug("The HttpSession is currently null, and the "
                                + HttpSessionSecurityContextRepository.class.getSimpleName()
                                + " is prohibited from creating an HttpSession "
                                + "(because the allowSessionCreation property is false) - SecurityContext thus not "
                                + "stored for next request");
                return null;
            }
            // Generate a HttpSession only if we need to

            if(contextObject.equals(context)) {
                LOGGER.debug("HttpSession is null, but SecurityContext has not changed from default empty context: ' "
                        + context
                        + "'; not creating HttpSession or storing SecurityContext");

                return null;
            }

            LOGGER.debug("HttpSession being created as SecurityContext is non-default");

            try {
                return request.getSession(true);
            } catch (IllegalStateException e) {
                // Response must already be committed, therefore can't create a new session
                LOGGER.warn("Failed to create a session, as response has been committed. Unable to store" +
                        " SecurityContext.");
            }

            return null;
        }
    }

    /**
     * Sets the {@link AuthenticationTrustResolver} to be used. The default is
     * {@link AuthenticationTrustResolverImpl}.
     *
     * @param trustResolver
     *            the {@link AuthenticationTrustResolver} to use. Cannot be
     *            null.
     */
    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        Assert.notNull(trustResolver, "trustResolver cannot be null");
        this.trustResolver = trustResolver;
    }

}
