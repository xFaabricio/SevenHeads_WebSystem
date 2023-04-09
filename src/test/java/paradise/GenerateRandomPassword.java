package paradise;

import org.apache.commons.lang3.RandomStringUtils;

public class GenerateRandomPassword {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%&*()-_=+[{]}\'\"<.>/?";
		String pwd = RandomStringUtils.random( 8, characters );
		System.out.println( pwd );
	}

}
