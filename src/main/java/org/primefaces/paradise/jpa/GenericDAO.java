package org.primefaces.paradise.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.Transactional;

public class GenericDAO<T> {

	@Transactional
	public T save(T entity) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();		
		entityManager.persist(entity);
		entityManager.flush();
		entityTransaction.commit();
		return entity;
	}
	
	@Transactional
	public T remove(T entity) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.remove(entity);
		entityTransaction.commit();
		return entity;
	}
	
	@Transactional
	public T update(T entity) {
		EntityManager entityManager = EntityFactory.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.merge(entity);
		entityTransaction.commit();
		return entity;
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
