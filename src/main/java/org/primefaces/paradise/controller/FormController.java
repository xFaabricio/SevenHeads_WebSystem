package org.primefaces.paradise.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.primefaces.paradise.entity.FormService;
import org.primefaces.paradise.entity.User;
import org.primefaces.paradise.jpa.EntityFactory;
import org.primefaces.paradise.jpa.GenericDAO;
import org.primefaces.paradise.repository.FormRepository;
import org.springframework.stereotype.Controller;

@Controller
@ViewScoped
public class FormController extends GenericDAO<FormService> implements FormRepository, Serializable {

	private static final long serialVersionUID = 7793679660548152402L;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FormService> findByUser(User user){
		EntityManager entityManager = EntityFactory.getEntityManager();
		Query query = entityManager.createQuery("SELECT formService FROM FormService formService WHERE formService.active = :active AND formService.idUser = :idUser");
		query.setParameter("active", true);
		query.setParameter("idUser", user.getId());
		try {
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public FormService saveEntity(FormService formService) {
		return save(formService);
	}
	
	@Override
	public FormService updateEntity(FormService formService) {
		return update(formService);
	}
	
}
