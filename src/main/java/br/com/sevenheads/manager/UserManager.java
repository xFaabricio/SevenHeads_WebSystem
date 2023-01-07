package br.com.sevenheads.manager;

import br.com.sevenheads.entity.User;

public interface UserManager {

	public User findUserByLogin(String login);
	
	public void addTry(User user);
}
