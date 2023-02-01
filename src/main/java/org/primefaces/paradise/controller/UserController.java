package org.primefaces.paradise.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.primefaces.paradise.entity.Role;
import org.primefaces.paradise.entity.User;
import org.primefaces.paradise.jpa.EntityFactory;
import org.primefaces.paradise.jpa.GenericDAO;
import org.primefaces.paradise.repository.UserRepository;
import org.springframework.stereotype.Controller;

@Controller
public class UserController extends GenericDAO<User> implements UserRepository {

	@Override
	public User findByLogin(String login) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		Query query = entityManager.createQuery("SELECT user FROM User user WHERE user.login = :login");
		query.setParameter("login", login);
		return (User) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> findUserRoles(User user){
		EntityManager entityManager = EntityFactory.getEntityManager();
		Query query = entityManager.createQuery("SELECT user.roles FROM User user WHERE user.id = :idUser");
		query.setParameter("idUser", user.getId());
		return (List<Role>) query.getResultList();
	}
	
}
