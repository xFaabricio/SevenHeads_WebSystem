package br.com.sevenheads.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public final class AuthenticationSuccesHandlerManagerImpl implements AuthenticationSuccessHandler {

	private static final Log LOGGER = LogFactory.getLog(AuthenticationSuccesHandlerManagerImpl.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
		UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        response.sendRedirect(request.getContextPath() + "/init.xhtml");
	}

}
