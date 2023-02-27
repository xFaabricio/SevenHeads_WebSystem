package org.primefaces.paradise.repository;

import java.util.List;

import org.primefaces.paradise.entity.Role;
import org.primefaces.paradise.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface RoleRepository {

	Role findByKey(String key);
	
	List<Role> findRolesByUser(User user);
}
