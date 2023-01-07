package br.com.sevenheads.manager;

import java.util.List;

public interface GenericDAO<T> {

	public T save(T entity);
	
	public T remove(T entity);
	
	public T update(T entity);
	
	public T findById(Long id, Class<?> classEntity);
	
	public List<T> findAll(Class<?> classEntity);
}
