package org.primefaces.paradise.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.primefaces.paradise.entity.Role;
import org.primefaces.paradise.entity.User;
import org.primefaces.paradise.repository.GuestPreferenceRepository;
import org.primefaces.paradise.repository.RoleRepository;
import org.primefaces.paradise.repository.UserRepository;
import org.primefaces.paradise.service.SmsService;
import org.primefaces.paradise.view.GuestPreferences;
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
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private GuestPreferenceRepository guestPreferenceRepository;
	
	SmsService service = new SmsService();
	
	String userLogin;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(username);
		
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}		
		
		if(user.getGuestPreferences() == null) {
			GuestPreferences userPreferences = new GuestPreferences();
			userPreferences.setLayout("default");
			userPreferences.setMenuMode("layout-menu-slim");
			userPreferences.setDarkMenu(true);
			userPreferences.setDarkTheme(false);
			userPreferences.setLogoBlack(true);
			userPreferences.setTheme("blue");
			userPreferences.setInputStyle("outlined");
			userPreferences.setUser(user);
			userPreferences = guestPreferenceRepository.saveEntity(userPreferences);
			
			user.setGuestPreferences(userPreferences);
			userRepository.updateEntity(user);
		}
		
		setUserLogin(user.getLogin());
		
		//Second Parameter is if user is enabled but spring security will redirect correctly with AuthenticationSuccessHandlerManagerImpl		
		return new UserSystem(user, true, true, true, true, getRoles(user));
	}
		
	private Collection<? extends GrantedAuthority> getRoles(User user){
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		
		List<Role> userRoles = roleRepository.findRolesByUser(user);
		
		for(Role role : userRoles) {
			roles.add(new SimpleGrantedAuthority(role.getKey()));
		}
		
		return roles;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	
}
