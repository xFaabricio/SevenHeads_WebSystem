package org.primefaces.paradise.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -8404557357192360389L;

	private String username;
	
	private String password;
	
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
}