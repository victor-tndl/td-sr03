package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Chat extends ActiveRecordBase {

	String title;
	String beginDate;
	String endDate;
  	User owner = null;

	public Chat(String _title, String _beginDate, String _endDate, User _owner) {
		super();
		this.title = _title;
		this.beginDate = _beginDate;
		this.endDate = _endDate;
		this.owner = _owner;
	}

	public Chat(ResultSet res) throws SQLException, ClassNotFoundException, IOException {
		this.id = res.getInt("id");
		this.title = res.getString("title");
		this.beginDate = res.getString("begin_date");
		this.endDate = res.getString("end_date");
		this.owner = User.findById(res.getInt("owner_id"));
        this._buitFromDB = true;
	}

	@Override
    public String toString() {
        return "Chat {" + "title=" + title + ", begin_date=" + beginDate + ""
               + ", end_date=" + endDate  + ", owner=" + owner.getFamilyName() + '}';
    }

	// Setters and getters
	public int getId() {
		return id;
	}
	public void setId(int _id) {
		this.id = _id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String _title) {
		this.title = _title;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String _beginDate) {
		this.beginDate = _beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String _endDate) {
		this.endDate = _endDate;
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
		return "INSERT INTO chat (title, begin_date, end_date, owner_id)"
				+ " VALUES("
				+ "'" + title +  "',"
				+ "'" + beginDate +  "',"
				+ "'" + endDate +  "',"
				+ "'" + owner.getId() +  "');";
	}

	@Override
	protected String _update() {
		return "UPDATE chat"
				+ " SET title=" + title
				+ ", SET begin_date=" + beginDate
				+ ", SET end_date=" + endDate
				+ ", SET owner_id=" + owner.getId()
				+ ", WHERE id=" + id;
	}

	@Override
	protected String _delete() {
		return "DELETE FROM chat WHERE id=" + id;
	}

	public static Chat findByTitle(String _title) throws ClassNotFoundException, IOException, SQLException {
        
        Connection conn = ConfigConnectionClass.getConnection();
		String query = "SELECT * FROM chat WHERE title=?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, _title);

		ResultSet res = ps.executeQuery();

        if (res.next()) {
            Chat chat = new Chat(res);
            return chat;
        }

        return null;	
	}

	public static List<Chat> findAll() throws ClassNotFoundException, IOException, SQLException {
		List <Chat> chats = new ArrayList<Chat>() ;
        
        Connection conn = ConfigConnectionClass.getConnection();
        Statement sql = conn.createStatement();
        ResultSet res = sql.executeQuery("SELECT * FROM chat");

        while (res.next()) {
            Chat newChat= new Chat(res);
            chats.add(newChat);
        }

        return chats;	
	}

	public static List<Chat> findByUser(User _user) throws ClassNotFoundException, IOException, SQLException {
		List <Chat> chats = new ArrayList<Chat>() ;
        
        Connection conn = ConfigConnectionClass.getConnection();
		String query = "SELECT * FROM chat WHERE owner_id=?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, String.valueOf(_user.getId()));

		ResultSet res = ps.executeQuery();

        while (res.next()) {
            Chat newChat= new Chat(res);
            chats.add(newChat);
        }

        return chats;	
	}

	public static Chat findById(int _id) throws ClassNotFoundException, IOException, SQLException {
        
        Connection conn = ConfigConnectionClass.getConnection();
		String query = "SELECT * FROM chat WHERE id=?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, String.valueOf(_id));

		ResultSet res = ps.executeQuery();

        if (res.next()) {
            Chat chat = new Chat(res);
            return chat;
        }

        return null;	
	}
}
