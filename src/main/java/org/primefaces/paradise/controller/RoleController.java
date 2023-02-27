package org.primefaces.paradise.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.primefaces.paradise.entity.Role;
import org.primefaces.paradise.entity.User;
import org.primefaces.paradise.jpa.EntityFactory;
import org.primefaces.paradise.jpa.GenericDAO;
import org.primefaces.paradise.repository.RoleRepository;
import org.springframework.stereotype.Controller;

@Controller
@ViewScoped
public class RoleController extends GenericDAO<Role> implements RoleRepository, Serializable {

	private static final long serialVersionUID = -8382958045552485449L;

	@Override
	public Role findByKey(String key) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		Query query = entityManager.createQuery("SELECT role FROM Role role WHERE role.key = :key");
		query.setParameter("key", key);		
		try {
			return (Role) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> findRolesByUser(User user) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		Query query = entityManager.createQuery("SELECT user.roles FROM User user WHERE user.id = :idUser");
		query.setParameter("idUser", user.getId());
		return (List<Role>) query.getResultList();
	}	
	
}
