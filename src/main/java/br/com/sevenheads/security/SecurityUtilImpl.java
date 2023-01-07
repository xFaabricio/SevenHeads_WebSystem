package br.com.sevenheads.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.sevenheads.entity.User;

@Component
public class SecurityUtilImpl {

	private static final long serialVersionUID = 1L;

	public static User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null) {
			return null;
		}

		UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();
		if(principal == null) {
			return null;
		}

		return principal.getUser();
	}
	
	public List<GrantedAuthority> getAuthoritiesByUser(Integer idUser) {		
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		return grantedAuthorities;
	}
}
