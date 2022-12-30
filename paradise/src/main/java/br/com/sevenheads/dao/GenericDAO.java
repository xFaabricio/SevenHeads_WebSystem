package br.com.sevenheads.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.Transactional;

import br.com.sevenheads.jpa.EntityFactory;

public class GenericDAO<T> {
	
	@Transactional
	public void save(T entity) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();		
		entityManager.persist(entity);
		entityManager.flush();
		entityTransaction.commit();		
	}
	
	@Transactional
	public void remove(T entity) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.remove(entity);
		entityTransaction.commit();
	}
	
	@Transactional
	public void update(T entity) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.merge(entity);
		entityTransaction.commit();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public T findById(Long id, Class<?> classEntity) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		try {
			return (T) entityManager.find(classEntity, id);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<?> classEntity){
		EntityManager entityManager = EntityFactory.getEntityManager();
		Query query = entityManager.createQuery("SELECT e FROM " + classEntity.getName() + " e");		
		return query.getResultList();
	}
	
}
