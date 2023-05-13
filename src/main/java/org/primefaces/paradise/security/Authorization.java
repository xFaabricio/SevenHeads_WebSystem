package org.primefaces.paradise.security;

import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.paradise.controller.UserController;
import org.primefaces.paradise.entity.Role;
import org.primefaces.paradise.entity.User;
import org.springframework.stereotype.Component;

@Named
@Component
public class Authorization {

	@Inject
	UserController userController;
	
	public boolean isAdmin() {
		User user = userController.findByLogin(UserDetailsUtil.getLoggedUser().getUsername());
		for(Role role : user.getRoles()) {
			if(role.getKey().equals("ROLE_ADMIN")) {
				return true;
			}
		}
		return false;
	}
	
}
