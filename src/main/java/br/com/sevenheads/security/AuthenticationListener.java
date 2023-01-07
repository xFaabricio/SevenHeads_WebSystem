package br.com.sevenheads.security;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthenticationListener implements ApplicationListener<AuthenticationSuccessEvent>{
	
	@SuppressWarnings("unused")
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		Authentication auth  = (Authentication)event.getAuthentication();
        WebAuthenticationDetails details = (WebAuthenticationDetails)auth.getDetails();
        UserDetailsImpl currentUser = (UserDetailsImpl)auth.getPrincipal();        
	}
} 
