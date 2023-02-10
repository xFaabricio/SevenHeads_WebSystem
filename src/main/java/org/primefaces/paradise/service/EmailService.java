package org.primefaces.paradise.service;

import javax.mail.MessagingException;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;

public class EmailService {

	@SuppressWarnings("deprecation")
	public void sendEmail(String to, String subject, String body) throws MessagingException {
        try {
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                    new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                            .configure(params.properties().setFileName("/configuration.properties"));
            PropertiesConfiguration config = builder.getConfiguration();

            String host = config.getString("mail.host");
            int port = config.getInt("mail.port");
            String username = config.getString("mail.username");
            String password = config.getString("mail.password");
            String mailFrom = config.getString("mail.from");
            
            SimpleEmail simpleEmail = new SimpleEmail();
            simpleEmail.setHostName(host);
            simpleEmail.setSmtpPort(port);
            simpleEmail.setAuthenticator(new DefaultAuthenticator(username, password));
            simpleEmail.setSSLOnConnect(true);
            simpleEmail.setTLS(true);
            simpleEmail.setStartTLSEnabled(true);
            simpleEmail.setStartTLSRequired(true);
            simpleEmail.setFrom(mailFrom);
			simpleEmail.addTo(to);
			simpleEmail.setSubject(subject);
			simpleEmail.setMsg(body);
			simpleEmail.send();            
        } catch (Exception e) {
            // trate exceções aqui
        }        
    }	
}
