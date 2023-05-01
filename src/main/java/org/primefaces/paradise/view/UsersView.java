package org.primefaces.paradise.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.paradise.controller.UserController;
import org.primefaces.paradise.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Named
@ViewScoped
public class UsersView implements Serializable {

	private static final long serialVersionUID = -8645506285021273066L;

	private List<User> selectedUsers;
	
	private User selectedUser;
	
	private List<User> users;
	
	private String newPasswordConfirmation;
	
	private String newPassword;
	
	@Inject
	UserController userController;
	
	@PostConstruct
    public void init() {
		users = userController.findAllActive();
	}
	
	public List<User> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<User> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getNewPasswordConfirmation() {
		return newPasswordConfirmation;
	}

	public void setNewPasswordConfirmation(String newPasswordConfirmation) {
		this.newPasswordConfirmation = newPasswordConfirmation;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void block(User user) {
		user.setBlocked(true);
		userController.update(user);
	}
	
	public void unblock(User user) {
		user.setBlocked(false);
		userController.update(user);
	}
	
	public void deleteSelectedUsers() {
		for(User usr : this.selectedUsers) {
			usr.setActive(false);
			userController.update(usr);
		}		
        this.users.removeAll(this.selectedUsers);
        this.selectedUsers = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Users Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
    }
	
	public String getDeleteButtonMessage() {
        if (hasSelectedUsers()) {
            int size = this.selectedUsers.size();
            return size > 1 ? size + " users selected" : "1 user selected";
        }

        return "Delete";
    }
	
	public boolean hasSelectedUsers() {
        return this.selectedUsers != null && !this.selectedUsers.isEmpty();
    }
	
	public void saveUser() {
		boolean update = true;
		
        if (this.selectedUser.getId() == null) {            
            this.users.add(this.selectedUser);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Added"));
        }
        else {        	
        	if((this.newPassword != null && !this.newPassword.equals("")) || (this.newPasswordConfirmation != null && !this.newPasswordConfirmation.equals(""))){
        		if(this.newPassword.equals(this.newPasswordConfirmation)) {
        			BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        			this.selectedUser.setPassword(bcrypt.encode(this.newPassword));
        			userController.update(this.selectedUser);        			
        		}else {
        			update = false;
        			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password and confirmation password do not match"));
        			PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
        		}
        	}
        	
        	if(update) {
	        	userController.update(this.selectedUser);
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Updated"));
        	}
        }
        
        if(update) {
	        PrimeFaces.current().executeScript("PF('manageUserDialog').hide()");
	        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
        }
    }

    public void deleteUser() {        
        this.selectedUser.setActive(false);
        userController.update(this.selectedUser);
        this.users.remove(this.selectedUser);
        this.selectedUser = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-users");
    }	
}
