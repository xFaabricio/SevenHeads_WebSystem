package org.primefaces.paradise.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.paradise.controller.RoleController;
import org.primefaces.paradise.controller.UserController;
import org.primefaces.paradise.entity.Role;
import org.primefaces.paradise.entity.User;
import org.primefaces.paradise.service.EmailService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -8404557357192360389L;

	private String username;
	
	private String password;
	
	private String email;
	
	@Inject
	UserController userController;
	
	@Inject
	RoleController roleController;
	
	public void submit() {		
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.getSessionMap().put("username", username);
		context.getSessionMap().put("password", password);
		context.setResponseContentType("application/x-www-form-urlencoded");
		RequestDispatcher dispatcher = ((HttpServletRequest) context.getRequest()).getRequestDispatcher("/login?username="+username+"&password="+password);
		try {
			dispatcher.forward((HttpServletRequest) context.getRequest(), (HttpServletResponse) context.getResponse());
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().responseComplete();	
	}
	
	public String redirectSignUp() {		
		return "signUp.xhtml?faces-redirect=true";
	}
	
	public String redirectSignIn() {		
		return "login.xhtml?faces-redirect=true";
	}	
	
	public void verifyUsername(FacesContext facesContext, UIComponent component, String value) {				
		if(value != null) {		
			User user = userController.findByLogin(value);
			
			if(user != null) {			
				FacesMessage msg = new FacesMessage("Username error","Username already exists !");
		        msg.setSeverity(FacesMessage.SEVERITY_INFO);
		        throw new ValidatorException(msg);
			}
		} 
	}	
	
	public void verifyEmail(FacesContext facesContext, UIComponent component, String value) {
		if(value != null) {
			User user = userController.findByEmail(value);
			
			if(user != null) {			
				FacesMessage msg = new FacesMessage("Email error","E-mail previously used, recover your account !");
		        msg.setSeverity(FacesMessage.SEVERITY_INFO);
		        throw new ValidatorException(msg);
			}
		}
	}
	
	public void register() {
		FacesContext context = FacesContext.getCurrentInstance();
		
		Boolean error = false;
		
		if(this.email == null || this.email.equals("")) {
			FacesMessage msg = new FacesMessage("E-mail error","E-mail is required");
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, msg);
	        error = true;
		}
		
		if(this.username == null || this.username.equals("")) {
			FacesMessage msg = new FacesMessage("Username error","Username is required");
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, msg);
	        error = true;
		}
		
		if(this.password == null || this.password.equals("")) {
			FacesMessage msg = new FacesMessage("Password error","Password is required");
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, msg);
	        error = true;
		}		
		
		if(!error) {
			try {
				createUser(this.email, this.username, this.password);
				
				try {
					EmailService emailService = new EmailService();
					emailService.sendEmailWelcome(email, username);
				} catch (Exception e) {
					// DO NOTHING
				}
								
				FacesMessage msg = new FacesMessage("Register success","Registered user !");
		        msg.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, msg);		        
		        return;
			} catch (Exception e) {
				FacesMessage msg = new FacesMessage("Register error","Contact administrator !");
		        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		        context.addMessage(null, msg);		        		        
			}			
		}		
	}
	
	public void createUser(String email, String username, String password) {
		User user = new User();
		user.setActive(true);
		user.setCreateDate(new Date());
		user.setEmail(email);
		user.setLogin(username);
		
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();		
		user.setPassword(bcrypt.encode(password));
		
		user.setFirstLogin(true);
		user.setVerified(false);
		
		Role role = roleController.findByKey("ROLE_USER");
		user.setRoles(Arrays.asList(role));
		
		user = userController.save(user);		
		userController.saveDefaultGuestPreferences(user);		
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}