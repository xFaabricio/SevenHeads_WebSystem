package org.primefaces.paradise.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsUtil {
	
	public static UserDetails getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null) {
			return null;
		}

		UserDetails principal = (UserDetails) auth.getPrincipal();
		if(principal == null) {
			return null;
		}
		
		return principal;
	}
	
}
