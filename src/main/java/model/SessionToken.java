package model;

import java.util.Date;

public class SessionToken {

	private Date beginDate; 
	private int userId;
	private String userFamilyName;
	private String userFirstName;
	private String userLogin;
	private String userRole;
	private boolean userIsAdmin = false;
	
	public SessionToken(User _user) {
		this.beginDate = new Date();
		this.userId = _user.getId();
		this.userFamilyName = _user.getFamilyName();
		this.userFirstName = _user.getFirstName();
		this.userLogin = _user.getLogin();
		this.userRole = _user.getRole();
		this.userIsAdmin = "Admin".equals(this.userRole);
	}

	@Override
    public String toString() {
        return "SessionToken {" + "familyName=" + userFamilyName + ", firstName=" + userFirstName + ""
               + ", login=" + userLogin + ", role=" + userRole + ", isAdmin=" + userIsAdmin +'}';
    }

	public Date getDate() {
		return beginDate;
	}
	// public void setDate(Date _beginDate) {
	// 	this.beginDate = _beginDate;
	// }
	public int getUserId() {
		return userId;
	}
	// public void getUserId(int _userId) {
	// 	this.userId = _userId;
	// }
	public String getUserFamilyName() {
		return userFamilyName;
	}
	// public void setUserFamilyName(String _userFamilyName) {
	// 	this.userFamilyName = _userFamilyName;
	// }
	public String getUserFirstName() {
		return userFirstName;
	}
	// public void setUserFirstName(String _userFirstName) {
	// 	this.userFirstName = _userFirstName;
	// }
	public String getUserLogin() {
		return userLogin;
	}
	// public void setUserLogin(String _userLogin) {
	// 	this.userLogin = _userLogin;
	// }
    public String getUserRole() {
		return userRole;
	}
	// public void setUserRole(String _userRole) {
	// 	this.userRole = _userRole;
	// }
	public boolean getUserIsAdmin() {
		return userIsAdmin;
	}
	// public void setUserIsAdmin(boolean _userIsAdmin) {
	// 	userIsAdmin = _userIsAdmin;
	// }
}
