package paradise;

import javax.mail.MessagingException;

import org.primefaces.paradise.service.EmailService;

public class Email {
	
	public static void main(String[] args) {
		EmailService emailService = new EmailService();

		try {
//			emailService.sendEmailWelcomeDebug("fabricio_oliveira1@hotmail.com", "Fabrício");
			emailService.sendEmailForgotDebug("fabricio.oliveira77@gmail.com", "xFaabricio", "test");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getStackTrace().toString());
		}
		
	}

}
