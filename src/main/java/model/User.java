package model;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
		Female,
		Male
	}

	public User(String _firstName, String _familyName, String _login, String _password, String _gender) {
		super();
		this.firstName = _firstName;
		this.familyName = _familyName;
		this.login = _login;
		this.password = _password;
		this.gender = Genders.valueOf(_gender);
	}
	
	public User(String _firstName, String _familyName) {
		this.firstName = _firstName;
		this.familyName = _familyName;
	}
	
	public User(ResultSet res) throws SQLException {
		this.id = res.getInt("id");
        this.firstName = res.getString("first_name");
        this.familyName = res.getString("family_name");
        this.login = res.getString("login");
        this.password = res.getString("password");
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

	// Setters and getters
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

	public static String hashPassword(String _password) {
		String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(_password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
		return generatedPassword;
	}

	// SQL queries
	@Override
	protected String _insert() {
		return "INSERT INTO user (family_name, first_name, login, password, is_admin, gender)"
				+ " VALUES("
				+ "'" + familyName +  "',"
				+ "'" + firstName +  "',"
				+ "'" + login +  "',"
				+ "'" + password +  "',"
				+ "'" + (role == Roles.Admin ? "1" : "0") +  "',"
				+ "'" + (gender == Genders.Male ? "1" : "0") +  "');";
	}

	@Override
	protected String _update() {
		return "UPDATE user"
				+ " SET first_name=" + firstName
				+ ", SET family_name=" + familyName
				+ ", SET login=" + login
				+ ", SET password=" + password
				+ ", SET gender=" + (gender == Genders.Male ? "1" : "0")
				+ ", SET is_admin = " + (role == Roles.Admin ? "1" : "0")
				+ ", WHERE id=" + id;
	}
	
	@Override
	protected String _delete() {
		return "DELETE FROM user WHERE id=" + id;
	}

	public static User findByFamilyNameAndFirstName(String _first_name, String _family_name) throws ClassNotFoundException, IOException, SQLException {
        
        Connection conn = ConfigConnectionClass.getConnection();
        Statement sql = conn.createStatement();
        ResultSet res = sql.executeQuery("SELECT * FROM user" 
			+ " WHERE first_name='" + _first_name + "'"
			+ " AND family_name='" + _family_name +"'" );

        if (res.next()) {
            User user = new User(res);
            return user;
        }

        return null;	
	}

	public static User findByLogin(String _login) throws ClassNotFoundException, IOException, SQLException {
        
        Connection conn = ConfigConnectionClass.getConnection();
        Statement sql = conn.createStatement();
        ResultSet res = sql.executeQuery("SELECT * FROM user" 
			+ " WHERE login='" + _login + "'");

        if (res.next()) {
            User user = new User(res);
            return user;
        }

        return null;	
	}

	public static User findById(int _id) throws ClassNotFoundException, IOException, SQLException {
        
        Connection conn = ConfigConnectionClass.getConnection();
        Statement sql = conn.createStatement();
        ResultSet res = sql.executeQuery("SELECT * FROM user" 
			+ " WHERE id='" + _id + "'");

        if (res.next()) {
            User user = new User(res);
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
            User newUser = new User(res);
            users.add(newUser);
        }

        return users;	
	}
}
