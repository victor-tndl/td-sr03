package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Forum extends ActiveRecordBase {

	String title;
	String begin_date;
	String end_date;
  	User owner = null;

	public Forum(String _title, String _begin_date, String _end_date, User _owner) {
		super();
		this.title = _title;
		this.begin_date = _begin_date;
		this.end_date = _end_date;
		this.owner = _owner;
	}

	public Forum(ResultSet res) throws SQLException, ClassNotFoundException, IOException {
		this.id = res.getInt("id");
		this.title = res.getString("title");
		this.begin_date = res.getString("begin_date");
		this.end_date = res.getString("end_date");
		this.owner = User.findById(res.getInt("owner_id"));
        this._buitFromDB = true;
	}

	@Override
    public String toString() {
        return "Forum {" + "title=" + title + ", begin_date=" + begin_date + ""
               + ", time validity=" + end_date  + ", owner=" + owner.getFamilyName() + '}';
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
	public String getBeginDate() {
		return begin_date;
	}
	public void setBeginDate(String _date) {
		this.begin_date = _date;
	}
	public String getEndDate() {
		return end_date;
	}
	public void setEndDate(String _end_date) {
		this.end_date = _end_date;
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
		return "INSERT INTO forum (title, begin_date, end_date, owner_id)"
				+ " VALUES("
				+ "'" + title +  "',"
				+ "'" + begin_date +  "',"
				+ "'" + end_date +  "',"
				+ "'" + owner.getId() +  "');";
	}

	@Override
	protected String _update() {
		return "UPDATE forum"
				+ " SET title=" + title
				+ ", SET begin_date=" + begin_date
				+ ", SET end_date=" + end_date
				+ ", SET owner_id=" + owner.getId()
				+ ", WHERE id=" + id;
	}

	@Override
	protected String _delete() {
		return "DELETE FROM forum WHERE id=" + id;
	}

	public static Forum findByTitle(String _title) throws ClassNotFoundException, IOException, SQLException {
        
        Connection conn = ConfigConnectionClass.getConnection();
		String query = "SELECT * FROM forum WHERE title=?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, _title);

		ResultSet res = ps.executeQuery();

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
		String query = "SELECT * FROM forum WHERE owner_id=?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, String.valueOf(_user.getId()));

		ResultSet res = ps.executeQuery();

        while (res.next()) {
            Forum newForum= new Forum(res);
            forums.add(newForum);
        }

        return forums;	
	}
}
