package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class User extends ActiveRecordBase {

	private int id;
    private static int autoIncrement=0;
	private String firstName;
	private String familyName;
	private String login;
	private String password;
	private String gender;
	private Roles role = Roles.Other;

	private enum Roles {
		Other,
		Admin
	}

	public User(String firstName, String familyName, String login, String password, String gender) {
		super();
		autoIncrement++;
		this.firstName = firstName;
		this.familyName = familyName;
		this.login = login;
		this.password = password;
		this.gender = gender;
		this.id = User.autoIncrement;
	}
	
	public User(String firstName, String familyName) {
		autoIncrement++;
		this.firstName = firstName;
		this.familyName = familyName;
	}
	
	public User(ResultSet res) throws SQLException {
		this.id = res.getInt("id");
        this.firstName = res.getString(2);
        this.familyName = res.getString(3);
        this.login = res.getString(4);
        this.gender = res.getString("gender");
        this.role = Roles.values()[res.getBoolean("is_admin") ? 1 : 0];
        this._buitFromDB = true;
	}

	@Override
    public String toString() {
        return "User{" + "familyName=" + familyName + ", firstName=" + firstName + ""
               + ", email=" + login  + ", gender=" + gender + ","
                + " password=" + password + '}';
    }
		
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String email) {
		this.login = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role.toString();
	}

	public void setRole(String role) {
		this.role = Roles.valueOf(role);
	}
	
	// SQL queries
	@Override
	protected String _insert() {
		return "INSERT INTO User"
				+ " VALUES(nextavl('id'), '"+ id 
				+ ", firstName=" + firstName
				+ ", familyName=" + familyName
				+ ", login=" + login
				+ ", password=" + password
				+ ", gender=" + gender
				+ ", `is_admin` = '" + (role == Roles.Admin ? "1" : "0")
				+ "')" ;
	}

	@Override
	protected String _update() {
		return "UPDATE User"
				+ " SET firstName=" + firstName
				+ "	SET familyName=" + familyName
				+ " SET login=" + login
				+ "	SET password=" + password
				+ "	SET gender=" + gender
				+ "	SET `is_admin` = '" + (role == Roles.Admin ? "1" : "0")
				+ " WHERE id=" + id;
	}
	
	@Override
	protected String _delete() {
		return "DELETE FROM User WHERE id=" + id;
	}

	public List<User> findAll() throws ClassNotFoundException, IOException, SQLException {
		List <User>  users = new ArrayList<User>() ;
        
        Connection conn = ConfigConnectionClass.getConnection();
        Statement sql = conn.createStatement();
        ResultSet res = sql.executeQuery("SELECT * FROM User");

        while (res.next()) {
            User newUser= new User (res);
            users.add(newUser);
        }

        return users;	
	}
}
