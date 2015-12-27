package com.zeshanaslam.aycserver.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		PreparedStatement statement = null;
		boolean isRegistered = false;
		try {
			String sql = "SELECT * FROM Users WHERE Username = '" + username + "';";
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
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
		PreparedStatement statement = null;

		try {
			String sql = "SELECT * FROM Users WHERE Username = '" + username + "';";
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();

			while(rs.next()) {
				loginObject = new LoginObject(rs.getString("Password"), rs.getString("Videos"), rs.getBoolean("Admin"));
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return loginObject;
	}
	
	public void updateYears(String videos, String uername) {
		try {
			String sql = "UPDATE Users SET "
					+ "Videos = ? where Username = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, videos);
			preparedStatement.setString(2, uername);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Sections
	 */
	public List<SectionObject> getSections(String year) {
		List<SectionObject> sections = new ArrayList<>();
		PreparedStatement statement = null;

		try {
			String sql = "SELECT * FROM Sections WHERE Year = '" + year + "';";
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				sections.add(new SectionObject(rs.getString("Name"), rs.getInt("ID")));
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sections;
	}

	public boolean sectionExists(String name) {
		PreparedStatement statement = null;
		boolean videoExists = false;
		try {
			String sql = "SELECT * FROM Sections WHERE Name = '" + name + "';";
			statement = connection.prepareStatement(sql);

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
			String sql = "UPDATE Sections SET "
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
	
	public void deleteSection(String section) {
		try {
			String sql = "DELETE FROM Sections WHERE Name = ?;";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, section);
			
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
		PreparedStatement statement = null;

		try {
			String sql = "SELECT * FROM Years;";
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				years.add(rs.getString("Name"));
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return years;
	}

	public List<String> getSize(String year) {
		List<String> years = new ArrayList<>();
		PreparedStatement statement = null;

		try {
			String sql = "SELECT * FROM Years WHERE Name = '" + year + "';";
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();

			while(rs.next()) {
				years.add(rs.getString("Name"));
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return years;
	}

	public void createYear(String year) {
		try {
			String sql = "INSERT INTO Years"
					+ "(Name) VALUES"
					+ "(?)";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, year);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteYear(String year) {
		try {
			String sql = "DELETE FROM Years WHERE Name = ?;";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, year);
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Create video
	 */
	public List<VideoObject> getVideos(String year, String section) {
		List<VideoObject> videoList = new ArrayList<>();
		PreparedStatement statement = null;
		
		try {
			String sql = "SELECT * FROM Videos WHERE Year = '" + year + "' AND Section = '" + section + "';";
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				videoList.add(new VideoObject(rs.getString("Name"), rs.getString("Desc"), rs.getString("Fileid"), rs.getInt("ID")));
			}
			
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return videoList;
	}

	public boolean videoExists(String year, String section) {
		PreparedStatement statement = null;
		boolean videoExists = false;
		try {
			String sql = "SELECT * FROM Videos WHERE Year = '" + year + "' AND Section = '" + section + "';";
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
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

	public int getLastID() {
		PreparedStatement statement = null;
		int ID = 0;
		try {
			String sql = "SELECT * FROM SQLITE_SEQUENCE WHERE name = 'Videos';";
			statement = connection.prepareStatement(sql);

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				ID = rs.getInt("seq");
				break;
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ID;
	}

	public void deleteVideo(int ID) {
		try {
			String sql = "DELETE FROM Videos WHERE ID = ?;";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, ID);
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateVideoSection(String year, String oldsection, String newsection) {
		try {
			String sql = "UPDATE Videos SET "
					+ "Section = ? where Section = ? AND Year = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newsection);
			preparedStatement.setString(2, oldsection);
			preparedStatement.setString(3, year);

			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getVideoByID(int ID) {
		String year = null;
		
		try {
		String sql = "SELECT * FROM Videos WHERE ID = ?;";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, ID);
		
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			year = rs.getString("Year");
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return year;
	}


	/*
	 * Default setup
	 */
	private void setupSQL() {
		PreparedStatement satement = null;

		try {
			//connection.setAutoCommit(false);

			String sql = "CREATE TABLE IF NOT EXISTS Years " +
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					" Name TEXT NOT NULL)"; 

			satement = connection.prepareStatement(sql);
			satement.executeUpdate();

			sql = "CREATE TABLE IF NOT EXISTS Sections " +
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					" Name TEXT NOT NULL, " + 
					" Year TEXT NOT NULL)"; 

			satement = connection.prepareStatement(sql);
			satement.executeUpdate();

			sql = "CREATE TABLE IF NOT EXISTS Users " +
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					"Username TEXT NOT NULL, " + 
					" Password TEXT NOT NULL, " +
					" Videos TEXT NOT NULL, " +
					" Admin BOOLEAN NOT NULL)"; 

			satement = connection.prepareStatement(sql);
			satement.executeUpdate();

			sql = "CREATE TABLE IF NOT EXISTS Videos " +
					"(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
					" Name TEXT NOT NULL, " + 
					" Desc TEXT NOT NULL, " +
					" Fileid TEXT NOT NULL, " +
					" Section TEXT NOT NULL, " +
					" Year TEXT NOT NULL)"; 

			satement = connection.prepareStatement(sql);
			satement.executeUpdate();

			satement.close();

			//connection.commit();
			//connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
