package org.primefaces.paradise.repository;

import org.primefaces.paradise.view.GuestPreferences;
import org.springframework.stereotype.Service;

@Service
public interface GuestPreferenceRepository {

	GuestPreferences saveEntity(GuestPreferences guestPreferences);
	
	GuestPreferences updateEntity(GuestPreferences guestPreferences);
	
	GuestPreferences findByLogin(String login);
}
