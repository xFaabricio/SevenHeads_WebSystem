package org.primefaces.paradise.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;

import org.primefaces.paradise.jpa.GenericDAO;
import org.primefaces.paradise.repository.GuestPreferenceRepository;
import org.primefaces.paradise.view.GuestPreferences;
import org.springframework.stereotype.Controller;

@Controller
@ViewScoped
public class GuestPreferencesController extends GenericDAO<GuestPreferences> implements GuestPreferenceRepository, Serializable {	
	
	private static final long serialVersionUID = -2132812444805451979L;

	public GuestPreferences saveEntity(GuestPreferences guestPreferences) {
		return save(guestPreferences);
	}
	
	public GuestPreferences updateEntity(GuestPreferences guestPreferences) {
		return update(guestPreferences);
	}
	
}
