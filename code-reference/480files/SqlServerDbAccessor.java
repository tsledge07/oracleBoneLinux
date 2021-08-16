package dao;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

public class SqlServerDbAccessor {

	private Connection con;
	private Statement stmt;
	private PreparedStatement prepStmt;
	private ResultSet rs;
	
	private String connectionUrl;
	
	private String defaultConnUrl = "jdbc:sqlserver://;" +
            "servername=csdata.cd4sevot432y.us-east-1.rds.amazonaws.com;"
			+ "user=csc312cloud;password=c3s!c2Cld;" 
			 + "databaseName=OBScript;";
	/*
	// in WSC
	private String defaultConnUrl = "jdbc:sqlserver://;servername=cssql\\sqldata;"
				+ "user=csc480dev;password=c4s*C0sWe;" +
			"databaseName=JLBookstore;";
	*/

	public SqlServerDbAccessor() {
		connectionUrl = defaultConnUrl;
	}
	
	public SqlServerDbAccessor(String serverName, String user, String pwd, 
			String dbName) {
		connectionUrl = "jdbc:sqlserver://;";
		connectionUrl += "servername=" + serverName + ";"; 
		connectionUrl += "user=" + user + ";"; 
		connectionUrl += "password=" + pwd + ";"; 
		connectionUrl += "databaseName=" + dbName + ";"; 
	}
	
	/*
	public void setDbName(String dbName) {
		connectionUrl += "databaseName=" + dbName;
	}
	*/
	
	public void connectToDb() {
    	try {
    		// Establish the connection.
    		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        	con = DriverManager.getConnection(connectionUrl);
    	} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Statement getStmt() {
		// TODO Auto-generated method stub
		return stmt;
	}

	public PreparedStatement getPrepStmt() {
		// TODO Auto-generated method stub
		return prepStmt;
	}

	public Connection getConnection() {
		// TODO Auto-generated method stub
		return con;
	}

	public String getUrl() {
		// TODO Auto-generated method stub
		return connectionUrl;
	}
	
	public boolean insertIntoSectionHeader(int i, Image image, String string) {
		boolean success = false;
		System.out.println("Insert BuShou"+i);
		try {
			prepStmt.setInt(1, i);
			prepStmt.setNString(3, string);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write((RenderedImage) image, "jpeg", os);                          
			// Passing: ​(RenderedImage im, String formatName, OutputStream output)
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			//imageInputStream = new FileInputStream(new File(imageFile));
			// set the second parameter in the query as binary stream 
			// from the specific file
			prepStmt.setBinaryStream(2, is);
			success = prepStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return success;
	}
		
	public void prepareInsertSectHdrStmt() {
		String query = "INSERT INTO SectionHeader VALUES (?, ?, ?)";
		System.out.println("Preparing...\n" + query);
		try {
			prepStmt = con.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
