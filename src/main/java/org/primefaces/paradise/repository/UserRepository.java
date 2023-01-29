package org.primefaces.paradise.repository;

import org.primefaces.paradise.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserRepository {

	User findByLogin(String login);
}
