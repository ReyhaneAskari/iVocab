package ca.concordia.ivocab.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class userprofile  implements IsSerializable{

	private String username;
	private String password;
	private String email;
	private long numberofwords = 0;
	
	
	public long getNumberofwords() {
		return numberofwords;
	}
	public void setNumberofwords(long numberofwords) {
		this.numberofwords = numberofwords;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public userprofile()
	{
		this.username = "";
		this.password = "";
		this.email = "";
	}
	public userprofile(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	public userprofile(String username, String password, String email)
	{
		this.username = username;
		this.password = password;
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
