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
	
	EmailService emailService = new EmailService();
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(username);
		
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		try {
			emailService.sendEmail("contato@sevenheads.com.br", "Teste envio de e-mail", "E-mail Enviado !!");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getStackTrace().toString());
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
