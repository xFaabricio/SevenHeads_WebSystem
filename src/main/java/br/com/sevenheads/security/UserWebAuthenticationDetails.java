package br.com.sevenheads.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class UserWebAuthenticationDetails extends WebAuthenticationDetails {

	/**
	 */
	private static final long serialVersionUID = 1L;
	private final String userAgent;
	private String realRemoteAddr;

	/**
	 */
	public UserWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		userAgent = request.getHeader("User-Agent");
		realRemoteAddr = request.getHeader("x-forwarded-for");
		if (realRemoteAddr == null) {
			realRemoteAddr = request.getHeader("X_FORWARDED_FOR");
			if (realRemoteAddr == null) {
				realRemoteAddr = request.getRemoteAddr();
			}
		}
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getRealRemoteAddr() {
		return realRemoteAddr;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserWebAuthenticationDetails [userAgent=");
		builder.append(userAgent);
		builder.append(", realRemoteAddr=");
		builder.append(realRemoteAddr);
		builder.append("]");
		return builder.toString();
	}

}
