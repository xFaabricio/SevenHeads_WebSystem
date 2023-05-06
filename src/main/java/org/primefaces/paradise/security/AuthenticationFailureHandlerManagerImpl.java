package org.primefaces.paradise.security;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.paradise.controller.UserController;
import org.primefaces.paradise.entity.User;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AuthenticationFailureHandlerManagerImpl implements AuthenticationFailureHandler {

	@Inject
	UserController userController;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		String userName = request.getParameter("username");
		User user = userController.findByLogin(userName);
		
		if(user != null && (user.getBlocked() == null || !user.getBlocked())) {			
			user.setTryQuantity(user.getTryQuantity() + 1);
			userController.update(user);
			if(user.getTryQuantity()>=3) {
				user.setBlocked(true);
				user.setBlockedDate(new Date());
				userController.update(user);
				response.sendRedirect(request.getContextPath() + "/login.xhtml?error=blockedUser");
			}			
		}
		
		if(user != null && (user.getBlocked() != null && !user.getBlocked())) {
			response.sendRedirect(request.getContextPath() + "/login.xhtml?error=blockedUser");
		}
		
		response.sendRedirect(request.getContextPath() + "/login.xhtml?error=loginError");
	}

}
