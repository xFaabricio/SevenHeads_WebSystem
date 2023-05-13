package org.primefaces.paradise.security;

import javax.inject.Named;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Named
@Component
public class Authorization {

	public boolean isAdmin() {		
		for(GrantedAuthority role : UserDetailsUtil.getLoggedUser().getAuthorities()) {
			if(role.getAuthority().equals("ROLE_ADMIN")) {
				return true;
			}
		}		
		return false;
	}
	
}
