package br.com.sevenheads.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import br.com.sevenheads.entity.User;

public final class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	private final User user;

	private final Map<String, GrantedAuthority> mapAuthorities;

	/**
	 */
	public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities) throws IllegalArgumentException {
		super(user.getLogin(), user.getPassword(), user.getActive(), true, true, true, authorities);

		this.user = user;
		this.mapAuthorities = new HashMap<String, GrantedAuthority>();
		for (GrantedAuthority grantedAuthority : this.getAuthorities()) {
			this.mapAuthorities.put(grantedAuthority.getAuthority(), grantedAuthority);
		}
	}

	/**
	 */
	public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities,
			boolean agree) {
		this(user, authorities);
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the mapAuthorities
	 */
	public Map<String, GrantedAuthority> getMapAuthorities(){
		return this.mapAuthorities;
	}

}
