package org.primefaces.paradise.converter;

public class Language {

	String locale;
	
	String flag;
	
	public Language() {
		super();
	}

	public Language(String locale, String flag) {
		super();
		this.locale = locale;
		this.flag = flag;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
