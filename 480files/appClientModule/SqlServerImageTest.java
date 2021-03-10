
//import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
//import java.util.ArrayList;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class SqlServerImageTest {
	// Declare the JDBC objects.
	Connection connection = null;
	Statement statement = null;

	public static void main(String[] args) {
		new SqlServerImageTest().run();
	}
	
	SqlServerImageTest() {
		setup();
	}

	private void setup() {
		// Create a variable for the connection string.
		String connectionUrl = "jdbc:sqlserver://;"
				+ "servername=csdata.cd4sevot432y.us-east-1.rds.amazonaws.com;" 
				+ "user=csc312cloud;password=c3s!c2Cld;" + 
				"databaseName=ChineseCharacters;";

		// Declare the JDBC objects.
		// Establish the connection.
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				
			connection = DriverManager.getConnection(connectionUrl);

			statement = connection.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void run() {
		// TODO Auto-generated method stub
		
		//simpleTest();
		
		insertIntoDB();
		
		retrieveFromDb();
	}

	private void insertIntoDB() {
		// TODO Auto-generated method stub
		PreparedStatement statement = null;
		FileInputStream imageInputStream = null;
		try {
            // set a pattern for the prepared statement 
			// with up to 3 images to insert
			String insertSQL = "insert into scripts " + 
			                  "values(?, ?, ?, ?, ?)";
			// get statement object  
		    statement = connection.prepareStatement(insertSQL);

		    // values for ID and images hard-coded for the 不 character
		    int ziID = 2; 
			// set the first parameter in the query with ziID
		    statement.setInt(1, ziID);

		    String uniCode = "4E2D"; //"5B78";
		    String imageFile = "ZiImage/" + uniCode +"XiaoZhuan.png";
		    // create an input stream pointing to the image file to store
			imageInputStream = new FileInputStream(new File(imageFile));
			// set the second parameter in the query as binary stream 
			// from the specific file
		    statement.setBinaryStream(2, imageInputStream);
		    
		    // repeat the process for setting image file ...
		    imageFile = "ZiImage/" + uniCode +"JinWen.png";
			imageInputStream = new FileInputStream(new File(imageFile));
		    statement.setBinaryStream(3, imageInputStream);
		    imageFile = "ZiImage/" + uniCode + "JiaGuWen.png";
			imageInputStream = new FileInputStream(new File(imageFile));
		    statement.setBinaryStream(4, imageInputStream);
              
		    // another time
		    //imageFile = "img/4E0Djs.png";
			//imageInputStream = new FileInputStream(new File(imageFile));
		    //statement.setBinaryStream(4, imageInputStream);
		    // could set a parameter to NULL when not providing a value
		    // for an image SQL Type (the second argument) is varbinary
		    statement.setString(5, uniCode);

		    // execute query
		    statement.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // close resources 
			if(imageInputStream!=null){
			     try {
					imageInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	//if (statement != null) try { statement.close(); } catch(Exception e) {}
	    	//if (connection != null) try { connection.close(); } catch(Exception e) {}
		}

	}

	private void retrieveFromDb() {
		ResultSet rs = null;
		
    	try {
    
    		// Create and execute an SQL statement that returns some data.
    		String SQL = "SELECT * FROM scripts";
    		rs = statement.executeQuery(SQL);
            ResultSetMetaData meta = rs.getMetaData();
            int columns = meta.getColumnCount();
            System.out.println(columns);
            for (int i=1; i<=columns; i++)
                System.out.print(meta.getColumnName(i) + ", ");
                
            System.out.println();
    
    		// Iterate through the data in the result set and display it.
            String curRow = "";
            TreeMap<Integer, String> hanZis = new TreeMap<Integer, String>();
    		while (rs.next()) {
                    for (int i=1; i<=columns; i++) {
                        System.out.print(rs.getString(i).trim() + 
                                ((i==columns)?"":", "));
						if (i==2) hanZis.put(rs.getInt(1),
								rs.getString(i));
                    }
    			//System.out.println(rs.getString(2) + " " + rs.getString(1));
    			//System.out.println(rs.wasNull());
                    System.out.println();
    		}
    		
    		String displaySQL = "select ziID, sealscript from scripts"//; 
    							+ " where ziid = 2";
    		rs = statement.executeQuery(displaySQL);
            String chnZi = "學";
            
            int ziId;
            while (rs.next()) {
            	ziId = rs.getInt(1);
                BufferedImage im = ImageIO.read(rs.getBinaryStream(2));
                ImageIcon ss = new ImageIcon(im);
                //chnZi = hanZis.get(ziId);
        		JOptionPane.showConfirmDialog(null, chnZi, "Chinese The Right Way - DB", 
        				JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, 
        				ss);
            }
    		
    	}
    
		// Handle any errors that may have occurred.
		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (rs != null) try { rs.close(); } catch(Exception e) {}
	    	//if (statement != null) try { statement.close(); } catch(Exception e) {}
	    	//if (connection != null) try { connection.close(); } catch(Exception e) {}
		}
	}

	private static void simpleTest() {
		// TODO Auto-generated method stub
		ImageIcon scriptImg = new ImageIcon(
				"C:\\courses\\IST220\\img\\767Dss.png");
		String chnZi = "學";
		// Generates a dialog box to show 
		// - chnZi as "message", and 
		// - its image as "icon"
		JOptionPane.showConfirmDialog(null, chnZi, "Chinese The Right Way", 
				JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, 
				scriptImg);
	}

}