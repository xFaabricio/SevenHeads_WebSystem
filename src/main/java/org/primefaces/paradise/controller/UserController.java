package org.primefaces.paradise.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.primefaces.paradise.entity.User;
import org.primefaces.paradise.jpa.EntityFactory;
import org.primefaces.paradise.jpa.GenericDAO;
import org.primefaces.paradise.repository.UserRepository;
import org.primefaces.paradise.view.GuestPreferences;
import org.springframework.stereotype.Controller;

@Controller
@ViewScoped
public class UserController extends GenericDAO<User> implements UserRepository, Serializable {

	private static final long serialVersionUID = 7793679660548152402L;

	@Inject
	GuestPreferencesController guestController;
	
	@Override
	public User findByLogin(String login) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		Query query = entityManager.createQuery("SELECT user FROM User user WHERE user.login = :login");
		query.setParameter("login", login);		
		try {
			return (User) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public User findByEmail(String email) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		Query query = entityManager.createQuery("SELECT user FROM User user WHERE user.email = :email");
		query.setParameter("email", email);		
		try {
			return (User) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public User saveEntity(User user) {
		return save(user);
	}
	
	@Override
	public User updateEntity(User user) {
		return update(user);
	}
	
	public void saveDefaultGuestPreferences(User user) {
		user.setGuestPreferences(defaultGuestPreferences(user));
		update(user);		
	}
	
	public GuestPreferences defaultGuestPreferences(User user) {
		GuestPreferences guestPreferences = new GuestPreferences();
		guestPreferences.setUser(user);
		guestController.saveEntity(guestPreferences);
		return guestPreferences;
	}
	
}
