package br.com.sevenheads.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import br.com.sevenheads.entity.User;
import br.com.sevenheads.manager.UserManager;


public class AuthenticationFailureHandlerManagerImpl implements AuthenticationFailureHandler{

	private static final Log LOGGER = LogFactory.getLog(AuthenticationFailureHandlerManagerImpl.class);
		
	private UserManager userManager;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String userName = request.getParameter("j_username");
		
		String pwd = request.getParameter("j_password");
		
		LOGGER.debug("CustomFilter Begins");        
		LOGGER.debug("CustomeFilter.username :: " + userName);
		LOGGER.debug("getMessage :: " + exception.getMessage());
		LOGGER.debug("exception :: " + exception.getClass().getSimpleName());
		LOGGER.debug("RemoteAddr :: " + request.getRemoteAddr());
		LOGGER.debug("Pwd :: " + pwd); 
        
        User user = userManager.findUserByLogin(userName);
        
        if (user != null) {        	
			userManager.addTry(user);			      	
        	
		   if( (user.getBlocked()!=null) && user.getBlocked()) {
	        	response.sendRedirect(request.getContextPath() + "/login.xhtml?error=blockedUser");
	        }else {
	        	response.sendRedirect(request.getContextPath() + "/login.xhtml?error=loginError");
	        }
		   
        }else {
        	response.sendRedirect(request.getContextPath() + "/login.xhtml?error=loginError");
        }      
        
	}


}
