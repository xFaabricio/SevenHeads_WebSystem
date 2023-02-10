package org.primefaces.paradise.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsService {

	// Substitua pelo seu número Twilio
    public static final String ACCOUNT_SID = "AC7602804ee85cb005abb3fc79d38201ba";
    // Substitua pela sua autenticação Twilio
    public static final String AUTH_TOKEN = "359b0e6ebb7cefc24906a92da5a9d834";
    // Substitua pelo número Twilio que você deseja utilizar para enviar mensagens
    public static final String TWILIO_PHONE_NUMBER = "+17622513376";

    public void sendSms(String to, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message sms = Message.creator(new PhoneNumber(to),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                message).create();

        System.out.println("Mensagem enviada a " + to + ": " + sms.getSid());
    }
	
}
