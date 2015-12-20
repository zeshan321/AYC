package com.zeshanaslam.aycserver.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zeshanaslam.aycserver.objects.LoginObject;
import com.zeshanaslam.aycserver.objects.SectionObject;
import com.zeshanaslam.aycserver.objects.VideoObject;

public class SQLite {

	private Connection connection = null;

	public SQLite() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:data.db");

			setupSQL();
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}

	/*
	 * Register
	 */
	public boolean isRegistered(String username) {
		Statement statement = null;
		boolean isRegistered = false;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "';");

			if (rs.next() && rs.getString("Username").equals(username)) {
				isRegistered = true;
			} else {
				isRegistered = false;
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isRegistered;
	}

	public void registerUser(String username, String password, String videos) {
		try {
			String sql = "INSERT INTO Users"
					+ "(Username, Password, Videos, Admin) VALUES"
					+ "(?,?,?,?)";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, videos);
			preparedStatement.setBoolean(4, false);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Login
	 */
	public LoginObject getLoginData(String username) {
		LoginObject loginObject = null;
		Statement statement = null;

		try {
			statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM Users WHERE Username = '" + username + "';");

			while(rs.next()) {
				loginObject = new LoginObject(rs.getString("password"), rs.getString("videos"), rs.getBoolean("admin"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return loginObject;
	}

	/*
	 * Sections
	 */
	public List<SectionObject> getSections(String year) {
		List<SectionObject> sections = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM Sections WHERE Year = '" + year + "';");

			while(rs.next()) {
				sections.add(new SectionObject(rs.getString("Name"), rs.getInt("ID")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sections;
	}
	
	public boolean sectionExists(String name) {
		Statement statement = null;
		boolean videoExists = false;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Sections WHERE Name = '" + name + "';");

			if (rs.next() && rs.getString("Name").equals(name)) {
				videoExists = true;
			} else {
				videoExists = false;
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return videoExists;
	}

	public void createSection(String name, String year) {
		try {
			String sql = "INSERT INTO Sections"
					+ "(Name, Year) VALUES"
					+ "(?,?)";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, year);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateSection(String name, String year, int ID) {
		try {
			String sql = "UPDATE Videos SET "
					+ "Name = ?, Year = ? where ID = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, year);
			preparedStatement.setInt(3, ID);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Years
	 */
	public List<String> getYears() {
		List<String> years = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM Years;");

			while(rs.next()) {
				years.add(rs.getString("Name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return years;
	}

	/*
	 * Create video
	 */
	public List<VideoObject> getVideos(String year, String section) {
		List<VideoObject> videoList = new ArrayList<>();
		Statement statement = null;
		
		try {
			statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT * FROM Videos WHERE Year = '" + year + "' AND Section = '" + section + "';");

			while(rs.next()) {
				videoList.add(new VideoObject(rs.getString("Name"), rs.getString("Desc"), rs.getString("Fileid"), rs.getInt("ID")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return videoList;
	}
	
	public boolean videoExists(String fileid) {
		Statement statement = null;
		boolean videoExists = false;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Videos WHERE Fileid = '" + fileid + "';");

			if (rs.next() && rs.getString("Fileid").equals(fileid)) {
				videoExists = true;
			} else {
				videoExists = false;
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return videoExists;
	}

	public void createVideo(String name, String desc, String fileid, String section, String year) {
		try {
			String sql = "INSERT INTO Videos"
					+ "(Name, Desc, Fileid, Section, Year) VALUES"
					+ "(?,?,?,?, ?)";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, desc);
			preparedStatement.setString(3, fileid);
			preparedStatement.setString(4, section);
			preparedStatement.setString(5, year);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateVideoString(String name, String desc, String fileid, String section, String year, int ID) {
		try {
			String sql = "UPDATE Videos SET "
					+ "Name = ?, Desc = ?, Fileid = ?, Section = ?, Year = ? where ID = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, desc);
			preparedStatement.setString(3, fileid);
			preparedStatement.setString(4, section);
			preparedStatement.setString(5, year);
			preparedStatement.setInt(6, ID);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setupSQL() {
		Statement satement = null;

		try {
			satement = connection.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS Years " +
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					" Name TEXT NOT NULL)"; 

			satement.executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS Sections " +
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					" Name TEXT NOT NULL, " + 
					" Year TEXT NOT NULL)"; 

			satement.executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS Users " +
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					"Username TEXT NOT NULL, " + 
					" Password TEXT NOT NULL, " +
					" Videos TEXT NOT NULL, " +
					" Admin BOOLEAN NOT NULL)"; 

			satement.executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS Videos " +
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					" Name TEXT NOT NULL, " + 
					" Desc TEXT NOT NULL, " +
					" Fileid TEXT NOT NULL, " +
					" Section TEXT NOT NULL, " +
					" Year TEXT NOT NULL)"; 

			satement.executeUpdate(sql);

			satement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
