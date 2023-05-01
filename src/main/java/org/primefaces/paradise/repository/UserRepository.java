package org.primefaces.paradise.repository;

import java.util.List;

import org.primefaces.paradise.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserRepository {

	User findByLogin(String login);
	
	User findByEmail(String email);
	
	User saveEntity(User user);
	
	User updateEntity(User user);
	
	List<User> findAllActive();
}
