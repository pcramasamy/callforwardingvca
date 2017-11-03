package cfvonage.vonage;

public class VonageAccount {
	
	private String u;
	private String p;
	public VonageAccount(String username, String password) {
		u = username;
		p = password;
	}
	
	public String getUsername() {
		return u;
	}
	
	public String getPassword() {
		return p;
	}

}
