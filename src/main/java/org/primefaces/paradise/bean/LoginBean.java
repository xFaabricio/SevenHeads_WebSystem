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
import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.primefaces.paradise.controller.RoleController;
import org.primefaces.paradise.controller.UserController;
import org.primefaces.paradise.entity.Role;
import org.primefaces.paradise.entity.User;
import org.primefaces.paradise.service.EmailService;
import org.primefaces.paradise.service.SmsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -8404557357192360389L;

	private String username;
	
	private String password;
	
	private String email;
	
	private String usernameRecovery;
	
	private String usernameSMS;
	
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
	
	public String redirectForgotPassword() {
		return "forgotPassword.xhtml?faces-redirect=true";
	}
	
	public String generateNewPassword() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%&*()-_=+[{]}\'\"<.>/?";		
		return RandomStringUtils.random( 8, characters );
	}
	
	public User recoveryProcessEmail(User user) {
		return recoveryProcess(user, true, false);
	}
	
	public User recoveryProcessSMS(User user) {
		return recoveryProcess(user, false, true);
	}
	
	public User recoveryProcess(User user, boolean email, boolean sms) {
		user.setChangePassword(true);		
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		String newPassword = generateNewPassword();
		user.setPassword(bcrypt.encode(newPassword));
		user.setBlocked(false);
		user = userController.update(user);
		
		if(email) {
			EmailService emailService = new EmailService();
			try {
				emailService.sendEmailForgot(user.getEmail(), user.getLogin(), newPassword);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(sms) {
			SmsService smsService = new SmsService();
			smsService.sendSms(user.getPhoneNumber(), templateSms(user.getLogin(),newPassword));
		}
		
		FacesMessage msg = new FacesMessage("Request sent","Your account recovery request has been sent.");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);        
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
		return user;
	}
	
	public String templateSms(String login, String newPassword) {		
		return "Your login is " + login + " and your new password is "+ newPassword +" . Log in and change your password immediately. If in doubt, please contact us.";
	}
	
	public void recoveryByEmail() {
		if(this.email != null && !this.email.equals("") ) {
			User user = userController.findByEmail(this.email);
			
			if(user == null) {			
				FacesMessage msg = new FacesMessage("User not found","We couldn't find a record with that email, create an account.");
		        msg.setSeverity(FacesMessage.SEVERITY_INFO);
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		        return;
			}
			
			user = recoveryProcessEmail(user);
		} else {
			FacesMessage msg = new FacesMessage("E-mail Required","Fill in the email field.");
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        return;
		}
	}
	
	public void recoveryByUsername() {
		if(this.usernameRecovery != null && !this.usernameRecovery.equals("") ) {
			User user = userController.findByLogin(this.usernameRecovery);
			
			if(user == null) {			
				FacesMessage msg = new FacesMessage("User not found","We couldn't find a record with that username, create an account.");
		        msg.setSeverity(FacesMessage.SEVERITY_INFO);
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		        return;
			}
			
			user = recoveryProcessEmail(user);
		} else {
			FacesMessage msg = new FacesMessage("Username Required","Fill in the username field.");
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        return;
		}
	}

	public void recoveryBySMS() {
		if(this.usernameSMS != null && !this.usernameSMS.equals("") ) {
			User user = userController.findByLogin(this.usernameSMS);
			
			if(user == null) {			
				FacesMessage msg = new FacesMessage("User not found","We couldn't find a record with that username, create an account.");
		        msg.setSeverity(FacesMessage.SEVERITY_INFO);
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		        return;
			}
			
			user = recoveryProcessSMS(user);
		} else {
			FacesMessage msg = new FacesMessage("Username Required","Fill in the username field.");
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	        return;
		}
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

	public String getUsernameRecovery() {
		return usernameRecovery;
	}

	public void setUsernameRecovery(String usernameRecovery) {
		this.usernameRecovery = usernameRecovery;
	}

	public String getUsernameSMS() {
		return usernameSMS;
	}

	public void setUsernameSMS(String usernameSMS) {
		this.usernameSMS = usernameSMS;
	}
	
}