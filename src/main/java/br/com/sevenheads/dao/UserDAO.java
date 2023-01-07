package br.com.sevenheads.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.sevenheads.entity.User;
import br.com.sevenheads.jpa.EntityFactory;

public class UserDAO {

	public User findByLogin(String login) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		Query query = entityManager.createQuery("SELECT user FROM User user WHERE user.login = :login");
		query.setParameter("login", login);
		return (User) query.getSingleResult();
	}
	
}
