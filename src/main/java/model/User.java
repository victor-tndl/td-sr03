package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class User extends ActiveRecordBase {

	private String firstName;
	private String familyName;
	private String login;
	private String password;
	private Genders gender = Genders.Male;
	private Roles role = Roles.Other;

	private enum Roles {
		Other,
		Admin
	}

	private enum Genders {
		Male,
		Female
	}

	public User(String firstName, String familyName, String login, String password, String gender) {
		super();
		this.firstName = firstName;
		this.familyName = familyName;
		this.login = login;
		this.password = password;
		this.gender = Genders.valueOf(gender);
	}
	
	public User(String firstName, String familyName) {
		this.firstName = firstName;
		this.familyName = familyName;
	}
	
	public User(ResultSet res) throws SQLException {
		this.id = res.getInt("id");
        this.firstName = res.getString(2);
        this.familyName = res.getString(3);
        this.login = res.getString(4);
        this.gender = Genders.values()[res.getBoolean("gender") ? 1 : 0];
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
		return gender.toString();
	}
	public void setGender(String gender) {
		this.gender = Genders.valueOf(gender);
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
		return "INSERT INTO user"
				+ " VALUES(nextavl('id'), '"+ id 
				+ ", first_name=" + firstName
				+ ", family_name=" + familyName
				+ ", login=" + login
				+ ", password=" + password
				+ ", gender=" + (gender == Genders.Male ? "1" : "0")
				+ ", `is_admin` = '" + (role == Roles.Admin ? "1" : "0")
				+ "')" ;
	}

	@Override
	protected String _update() {
		return "UPDATE user"
				+ " SET first_name=" + firstName
				+ "	SET family_name=" + familyName
				+ " SET login=" + login
				+ "	SET password=" + password
				+ "	SET gender=" + (gender == Genders.Male ? "1" : "0")
				+ "	SET `is_admin` = '" + (role == Roles.Admin ? "1" : "0")
				+ " WHERE id=" + id;
	}
	
	@Override
	protected String _delete() {
		return "DELETE FROM user WHERE id=" + id;
	}

	public static User findByFamilyNameAndFirstName(String firstNameParam, String familyNameParam ) throws ClassNotFoundException, IOException, SQLException {
        
        Connection conn = ConfigConnectionClass.getConnection();
        Statement sql = conn.createStatement();
        ResultSet res = sql.executeQuery("SELECT *FROM user" 
			+ "WHERE first_name=" + firstNameParam
			+ "family_name=" + familyNameParam );

        if (res.next()) {
            User user= new User(res);
            return user;
        }

        return null;	
	}

	public static List<User> findAll() throws ClassNotFoundException, IOException, SQLException {
		List <User>  users = new ArrayList<User>() ;
        
        Connection conn = ConfigConnectionClass.getConnection();
        Statement sql = conn.createStatement();
        ResultSet res = sql.executeQuery("SELECT * FROM user");

        while (res.next()) {
            User newUser= new User (res);
            users.add(newUser);
        }

        return users;	
	}
}
