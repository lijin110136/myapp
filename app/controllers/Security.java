package controllers;

public class Security extends Secure.Security{
	static boolean authenticate(String username, String password) {
		if("admin".equals(username) && "123".equals(password))
			return true;
		return false;
    }
}
