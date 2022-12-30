package paradise;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import br.com.sevenheads.dao.GenericDAO;
import br.com.sevenheads.entity.User;

public class UserDAO {

	@Inject
	static GenericDAO<User> genericDao = new GenericDAO<>();
	
	public static void main(String[] args) {		
		System.out.println(findAll());
	}
	
	public void createUser() {
		User user = new User();
		user.setActive(true);
		user.setBlocked(false);
		user.setCreateDate(new Date());
		user.setEmail("fabricio_oliveira1@hotmail.com");
		user.setLogin("MASTER");
		user.setPassword("MASTER");
		try {
			genericDao.save(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updateUser(User user) {
		genericDao.update(user);
	}
	
	public static void removeUser() {
		
	}
	
	public static User findUser() {
		User obj = genericDao.findById(1L, User.class);
		return obj;
	}
	
	public static List<User> findAll(){
		return genericDao.findAll(User.class);
	}
}
