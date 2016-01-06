import java.sql.*;

public class DatabaseCommunicator {
	Connection dbConnection = null;
	public DatabaseCommunicator()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
		
		try
		{
			String dbURL = "INSERT_PROPER_URL_HERE"; //TODO: Insert URL (jdbc:sqlite:<somename>.sqlite)
			dbConnection = DriverManager.getConnection(dbURL);
		}
		catch( SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	//TODO: SQL: Make request.
}
