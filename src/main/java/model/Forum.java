package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Forum extends ActiveRecordBase {

	String title;
	Date date;
	Date time_validity;
  	User owner = null;

	public Forum(String _title, Date _date, Date _time_validity, User _owner) {
		super();
		this.title = _title;
		this.date = _date;
		this.time_validity = _time_validity;
		this.owner = _owner;
	}

	public Forum(ResultSet res) throws SQLException, ClassNotFoundException, IOException {
		this.id = res.getInt("id");
		this.title = res.getString("title");
		this.date = res.getDate("date");
		this.time_validity = res.getDate("time_validity");
		this.owner = User.findById(res.getInt("owner_id"));
        this._buitFromDB = true;
	}

	@Override
    public String toString() {
        return "Forum {" + "title=" + title + ", date=" + date + ""
               + ", time validity=" + time_validity  + ", owner=" + owner.getFamilyName() + '}';
    }

	// Setters and getters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String _title) {
		this.title = _title;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date _date) {
		this.date = _date;
	}
	public Date getTimeValidity() {
		return time_validity;
	}
	public void setTimeValidity(Date _time_validity) {
		this.time_validity = _time_validity;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User _owner) {
		this.owner = _owner;
	}

	// SQL queries
	@Override
	protected String _insert() {
		return "INSERT INTO forum (title, date, time_validity, owner_id)"
				+ " VALUES("
				+ "'" + title +  "',"
				+ "'" + date +  "',"
				+ "'" + time_validity +  "',"
				+ "'" + owner.getId() +  "');";
	}

	@Override
	protected String _update() {
		return "UPDATE forum"
				+ " SET title=" + title
				+ ", SET date=" + date
				+ ", SET time_validity=" + time_validity
				+ ", SET owner_id=" + owner.getId()
				+ ", WHERE id=" + id;
	}

	@Override
	protected String _delete() {
		return "DELETE FROM forum WHERE id=" + id;
	}

	public static Forum findByTitle(String _title) throws ClassNotFoundException, IOException, SQLException {
        
        Connection conn = ConfigConnectionClass.getConnection();
        Statement sql = conn.createStatement();
        ResultSet res = sql.executeQuery("SELECT * FROM forum" 
			+ " WHERE title='" + _title + "'");

        if (res.next()) {
            Forum forum = new Forum(res);
            return forum;
        }

        return null;	
	}

	public static List<Forum> findAll() throws ClassNotFoundException, IOException, SQLException {
		List <Forum> forums = new ArrayList<Forum>() ;
        
        Connection conn = ConfigConnectionClass.getConnection();
        Statement sql = conn.createStatement();
        ResultSet res = sql.executeQuery("SELECT * FROM forum");

        while (res.next()) {
            Forum newForum= new Forum(res);
            forums.add(newForum);
        }

        return forums;	
	}

	public static List<Forum> findByUser(User _user) throws ClassNotFoundException, IOException, SQLException {
		List <Forum> forums = new ArrayList<Forum>() ;
        
        Connection conn = ConfigConnectionClass.getConnection();
        Statement sql = conn.createStatement();
        ResultSet res = sql.executeQuery("SELECT * FROM forum WHERE owner_id ='" + _user +"'");

        while (res.next()) {
            Forum newForum= new Forum(res);
            forums.add(newForum);
        }

        return forums;	
	}
}