package paradise;

import javax.persistence.Persistence;

public class DatabaseConnection {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Persistence.createEntityManagerFactory("sevenHeadsDB");
	}

}
