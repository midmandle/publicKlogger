import java.sql.*;

public class DatabaseCommunicator {
	private Connection dbConnection = null;
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
			String dbURL = "jdbc:sqlite:main.sqlite"; //Insert DB URL (jdbc:sqlite:<somename>.sqlite)
			dbConnection = DriverManager.getConnection(dbURL);
			
		}
		catch( SQLException e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	//TODO: SQL: Make request. Requires pre-formed statements (i.e. a pre determined statement for each SQL request).
	public ResultSet testMakeRequest(String query) throws SQLException
	{
		Statement statement = null;
		ResultSet resultSet = null;
		
		//query = "SELECT * FROM testTable;";
		
		try
		{
			statement = dbConnection.createStatement();
			System.out.println(query);
			resultSet = statement.executeQuery(query);
			//System.out.println(resultSet.getRow());
			while(resultSet.next())
			{
				System.out.println(resultSet.getInt(1)+" "+resultSet.getInt(2));
			}
			return resultSet;
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			
			if(resultSet != null)
				resultSet.close();
			if(resultSet != null)
				statement.close();
			if(dbConnection != null)
				dbConnection.close();
			
		}
		return resultSet;
	}
	
	public void setupNewAppointmentDatabase(String appointmentBookName)
	{
		Statement statement = null;
		
		try
		{
			statement = dbConnection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS "+ appointmentBookName +" (dateTimeFrom DATETIME PRIMARY KEY, dateTimeTo DATETIME UNIQUE, description VARCHAR, location VARCHAR);");
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
}
