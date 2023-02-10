package org.primefaces.paradise.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

public class EmailService {

	private Session session;

	private boolean configured;
	
	public void configureFromPropertiesFile() {
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

            configure(host, port, username, password);
        } catch (Exception e) {
            // trate exceções aqui
        	System.out.println(e.getStackTrace().toString());
        }
    }
	
    public void configure(String host, int port, final String username, final String password) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");                    
        properties.put("mail.smtp.starttls.enable","true");        
        
        // Use the following if you need SSL
        properties.put("mail.smtp.ssl.trust", host);
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        
        session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        configured = true;                   
    }

	public void sendEmail(String to, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        
        if(configured) {        	        
        	Transport.send(message);            
        } else {        	
        	configureFromPropertiesFile();        	
    		Transport.send(message);             	
        }
    }	
}
