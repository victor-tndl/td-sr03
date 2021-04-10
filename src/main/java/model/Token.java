package model;

import java.util.Date;

public class Token {

	private Date beginDate; 
	private String firstName;
	private String familyName;
	private String login;
	private String role;
	private boolean isAdmin = false;
	
	public Token(User _user) {
		this.beginDate = new Date();
		this.familyName = _user.getFamilyName();
		this.firstName = _user.getFirstName();
		this.login = _user.getLogin();
		this.role = _user.getRole();
		this.isAdmin = "Admin".equals(this.role);
	}

	@Override
    public String toString() {
        return "Token {" + "familyName=" + familyName + ", firstName=" + firstName + ""
               + ", login=" + login + ", role=" + role + ", isAdmin=" + isAdmin +'}';
    }

	public Date getDate() {
		return beginDate;
	}
	// public void setDate(Date _beginDate) {
	// 	this.beginDate = _beginDate;
	// }
	public String getFirstName() {
		return firstName;
	}
	// public void setFirstName(String _firstName) {
	// 	this.firstName = _firstName;
	// }
	public String getFamilyName() {
		return familyName;
	}
	// public void setFamilyName(String _familyName) {
	// 	this.familyName = _familyName;
	// }
	public String getLogin() {
		return login;
	}
	// public void setLogin(String _login) {
	// 	this.login = _login;
	// }
    public String getRole() {
		return role;
	}
	// public void setRole(String _role) {
	// 	this.role = _role;
	// }
	public boolean getIsAdmin() {
		return isAdmin;
	}
	// public void setIsAdmin(boolean _isAdmin) {
	// 	isAdmin = _isAdmin;
	// }
}
