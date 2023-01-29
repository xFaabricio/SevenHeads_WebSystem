package org.primefaces.paradise.security;

import java.util.Collection;

import org.primefaces.paradise.entity.User;
import org.springframework.security.core.GrantedAuthority;

public class UserSystem extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 5598463668086828751L;
	
	private User user;
	
	public UserSystem(User user, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(user.getLogin(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
