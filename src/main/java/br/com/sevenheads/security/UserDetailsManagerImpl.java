package br.com.sevenheads.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import br.com.sevenheads.entity.User;
import br.com.sevenheads.manager.UserManager;

@Service("userDetailManager")
public final class UserDetailsManagerImpl implements UserDetailsManager {

	private static final Log LOGGER = LogFactory.getLog(UserDetailsManagerImpl.class);

	private UserManager userManager;
	
	// Manager's
	private AuthenticationManager authenticationManager;

	@Autowired
	private HttpServletRequest request;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		if (login == null || login.equals("")) {
			String error = "Login deve ser informado.";
			LOGGER.info(error);
			throw new UsernameNotFoundException(error);
		}

		User user = userManager.findUserByLogin(login);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));		
		UserDetailsImpl detail = new UserDetailsImpl(user, grantedAuthorities);
		LOGGER.info("User " + detail + " successfully login.");
		return detail;
	}



	@Override
	public void changePassword(String oldPassword, String newPassword) {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

		if (currentUser == null) {
			// This would indicate bad coding somewhere
			throw new AccessDeniedException(
					"Can't change password as no Authentication object found in context " + "for current user.");
		}

		String username = currentUser.getName();

		// If an authentication manager has been set, re-authenticate the user with the
		// supplied password.
		if (authenticationManager != null) {
			LOGGER.debug("Reauthenticating user '" + username + "' for password change request.");

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		} else {
			LOGGER.debug("No authentication manager set. Password won't be re-checked.");
		}

		SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(currentUser, newPassword));
	}

	protected Authentication createNewAuthentication(Authentication currentAuth, String newPassword) {
		UserDetails user = loadUserByUsername(currentAuth.getName());

		UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(user,
				user.getPassword(), user.getAuthorities());
		newAuthentication.setDetails(currentAuth.getDetails());

		return newAuthentication;
	}

	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public String getFrom() {
		String ip = request.getRemoteAddr();
		LOGGER.info("RemoteAddr: " + ip);

		String headerClientIp = request.getHeader("Client-IP");
		LOGGER.info("Client-IP: " + headerClientIp);
		String headerXForwardedFor = request.getHeader("X-Forwarded-For");
		LOGGER.info("X-Forwarded-For: " + headerXForwardedFor);		

		return ip;
	}



	@Override
	public void createUser(UserDetails user) {
		// TODO Auto-generated method stub
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		grantedAuthorities.add(new SimpleGrantedAuthority("IS_AUTHENTICATED_ANONYMOUSLY"));		
		UserDetailsImpl detail = new UserDetailsImpl(new User(), grantedAuthorities);
	}



	@Override
	public void updateUser(UserDetails user) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void deleteUser(String username) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}
}
