package org.primefaces.paradise.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.MessagingException;

import org.primefaces.paradise.entity.Role;
import org.primefaces.paradise.entity.User;
import org.primefaces.paradise.repository.UserRepository;
import org.primefaces.paradise.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(username);
		
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return new UserSystem(user, !user.getBlocked(), true, true, true, getRoles(user));
	}
		
	private Collection<? extends GrantedAuthority> getRoles(User user){
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		
		List<Role> userRoles = userRepository.findUserRoles(user);
		
		for(Role role : userRoles) {
			roles.add(new SimpleGrantedAuthority(role.getKey()));
		}
		
		return roles;
	}

}
