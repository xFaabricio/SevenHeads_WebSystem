package org.primefaces.paradise.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.paradise.controller.UserController;
import org.primefaces.paradise.entity.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AuthenticationSuccessHandlerManagerImpl implements AuthenticationSuccessHandler {

	@Inject
	UserController userController;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("UNAUTHORIZED"));
		
		User user = userController.findByLogin(authentication.getName());
		if(user != null) {
			if((user.getBlocked() != null && user.getBlocked())) {
				invalidateSession(request);
				response.sendRedirect(request.getContextPath() + "/login.xhtml?error=blockedUser");			
				return;					
			} else {
				if(user.getActive() != null && Boolean.TRUE.equals(user.getActive())) {
					user.setTryQuantity(0);
					userController.update(user);
				}
			}
			
			if(user.getActive() != null && !user.getActive()) {			
				invalidateSession(request);
				response.sendRedirect(request.getContextPath() + "/login.xhtml?error=deletedUser");			
				return;
			}
		}
		
		response.sendRedirect(request.getContextPath() + "/dashboard.xhtml");
	}

	public void invalidateSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        SecurityContextHolder.clearContext();
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("UNAUTHORIZED"));
        Authentication emptyAuth = new AnonymousAuthenticationToken("empty", "empty", roles);
        SecurityContextHolder.getContext().setAuthentication(emptyAuth);        
	}
	
}
