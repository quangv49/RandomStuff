import java.io.Serializable;
import java.util.HashMap;
public class UserList implements Serializable {
	private static final long serialVersionUID = 694947020792147466L;
	private HashMap<String, User> list;
	
	public UserList() {
		list = new HashMap<>();
	}
	
	public void register(String username, String password) {
		list.put(username, new User(username, password));
	}
	
	public boolean authorize(String username, String password) {
		return list.containsKey(username) && list.get(username).getPassword().equals(password);
	}
	
	public boolean validate(String username) {
		return username != null && !username.equals("") && !list.containsKey(username);
	}
	
	public User get(String username) {
		return list.get(username);
	}
}
