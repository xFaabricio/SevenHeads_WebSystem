package br.com.sevenheads.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;

public class UserWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, UserWebAuthenticationDetails> {

	@Override
	public UserWebAuthenticationDetails buildDetails(HttpServletRequest context) {
		return new UserWebAuthenticationDetails(context);
	}
}
