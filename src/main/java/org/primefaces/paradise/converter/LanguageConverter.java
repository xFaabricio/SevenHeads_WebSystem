package org.primefaces.paradise.converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "languageConverter")
public class LanguageConverter implements Serializable, Converter<Object>{
	
	private static final long serialVersionUID = 8786871921844646700L;	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value.trim().equals("")) {
	        return null;
	    } else {
	    	List<Language> languageList = new ArrayList<>();	        
	        ResourceBundle message = ResourceBundle.getBundle("i18n.messages");        
	        languageList.add(new Language(message.getString("portuguese"), "Brazil"));
	        languageList.add(new Language(message.getString("english"), "United-States"));
	        languageList.add(new Language(message.getString("spanish"), "Spain"));
	        languageList.add(new Language(message.getString("chinese"), "China"));	    	
	    	
	        for (Language l : languageList) {
	            if (l.getFlag().equals(value)) {
	                return l;
	            }
	        }
	    }
	    return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null || value.equals("")) {
	        return "";
	    } else {
	        return String.valueOf(((Language) value).getFlag());
	    }
	}

}
