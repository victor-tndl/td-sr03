package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ActiveRecordBase {
	protected static int auto_increment = 0;
	protected int id;
	protected boolean _buitFromDB;
	
	public ActiveRecordBase() {
		this._buitFromDB = false;
	}
	
	public void save() throws SQLException, ClassNotFoundException, IOException {
		Connection _cx = ConfigConnectionClass.getConnection();
		Statement statement = _cx.createStatement();
		if (_buitFromDB) {
			System.out.println("Executing command:" + _update() + "\n");
			statement.executeUpdate(_update());
		} else {
			System.out.println("Executing command:" + _insert() + "\n");
			statement.executeUpdate(_insert(), Statement.RETURN_GENERATED_KEYS);
			_buitFromDB = true;
		}

		ResultSet result = statement.getGeneratedKeys();

		while (result.next()) {
			id = result.getInt(1);
		}
	}
	
	public void delete() throws SQLException, ClassNotFoundException, IOException {
		Connection _cx = ConfigConnectionClass.getConnection();
		Statement statement =  _cx.createStatement();
		if (_buitFromDB) {
			System.out.println("Executing command:" + _delete() + "\n");
			statement.executeUpdate(_delete());
		} else {
			System.out.println("Non persisatnt object");
		}
	}

	protected abstract String _delete();

	protected abstract String _insert();

	protected abstract String _update();

}