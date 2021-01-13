package api;

public interface LoginHandling {
		
	void createLogin(String username, String password);
	void login(String username, String password);
	void changePassword(String newPassword);
	void logout();
}
