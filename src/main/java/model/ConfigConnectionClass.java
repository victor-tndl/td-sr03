package model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConfigConnectionClass {

	private static Connection connection = null; 

	public ConfigConnectionClass() throws IOException, ClassNotFoundException, SQLException {
		Properties propsProperties = new Properties();
		URL urlFichierPropertiesUrl = ConfigConnectionClass.class.getResource("configDB.properties");
		BufferedInputStream buffer = null;
		try {
			buffer = new BufferedInputStream(urlFichierPropertiesUrl.openStream());
			propsProperties.load(buffer);
			String driver = propsProperties.getProperty("driver");
			String url = propsProperties.getProperty("url");
			String username = propsProperties.getProperty("username");
			String password = propsProperties.getProperty("password");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
		} finally {
			if (buffer != null) {
				buffer.close();
			}
		}
	}

	public static Connection getConnection() throws ClassNotFoundException, IOException, SQLException  {
		if (connection == null) {
			new ConfigConnectionClass();
		}
		return connection;
	}
}