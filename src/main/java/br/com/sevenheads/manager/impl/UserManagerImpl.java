package br.com.sevenheads.manager.impl;

import javax.inject.Inject;

import br.com.sevenheads.dao.GenericDAO;
import br.com.sevenheads.dao.UserDAO;
import br.com.sevenheads.entity.User;
import br.com.sevenheads.manager.UserManager;

public class UserManagerImpl extends GenericDAO<User> implements UserManager {

	@Inject
	UserDAO userDAO;
	
	@Override
	public User findUserByLogin(String login) {
		return userDAO.findByLogin(login);
	}

	@Override
	public void addTry(User user) {
		user.setTryQuantity(user.getTryQuantity() + 1);
		save(user);
	}

	
	
}
