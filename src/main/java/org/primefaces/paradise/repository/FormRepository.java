package org.primefaces.paradise.repository;

import java.util.List;

import org.primefaces.paradise.entity.FormService;
import org.primefaces.paradise.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface FormRepository {

	List<FormService> findByUser(User user);
	
	FormService saveEntity(FormService formService);
	
	FormService updateEntity(FormService formService);
	
}
