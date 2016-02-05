import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * A static class to handle the additional functionality offered by storing the AppointmentBook objects in a database.
 * @author 14061121
 *
 */
public class DatabaseCommunicator {
	private static Connection dbConnection = null;
	private static Statement statement = null;
	private static ResultSet resultSet = null;
	
	/**
	 * Method to make an SQL query which doesn't return any content from the Database (e.g. INSERT, DROP, CREATE).
	 * @param query the SQL query to execute.
	 */
	private static void MakeRequest(String query)
	{
		try
		{
			try
			{
				Class.forName("org.sqlite.JDBC");
				String dbURL = "jdbc:sqlite:main.sqlite"; //Insert DB URL (jdbc:sqlite:<somename>.sqlite)
				dbConnection = DriverManager.getConnection(dbURL);
				statement = dbConnection.createStatement();
				statement.execute(query);
			}
			finally
			{
				if(statement != null)
					statement.close();
				if(dbConnection != null)
					dbConnection.close();
			}
		}
		catch(SQLException | ClassNotFoundException e)
		{
			System.out.println("MakeRequest(String query)");
			System.out.println(e.getMessage());
		}
	}
	/**
	 * Method to make an SQL query which does return  content from the Database (e.g. SELECT).
	 * @param query the SQL query to execute.
	 */
	private static void MakeRequestWithOutput(String query)
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			String dbURL = "jdbc:sqlite:main.sqlite"; //Insert DB URL (jdbc:sqlite:<somename>.sqlite)
			dbConnection = DriverManager.getConnection(dbURL);
			statement = dbConnection.createStatement();
			
			resultSet = statement.executeQuery(query);
			
		}
		catch(SQLException | ClassNotFoundException e)
		{
			System.out.println("MakeRequestWithOutput(String query)");
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * A method to format the GregorianCalendar format used by the application to a form suitable for the DB.
	 * @param cal the GregorianCalendar object to format.
	 * @return a formatted string usable by the DB to represent date/time.
	 */
	private static String dateFormatter(GregorianCalendar cal)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setCalendar(cal);
		String output = sdf.format(cal.getTime());
		//System.out.println(output);
		return output;
	}
	
	/**
	 * A method to convert the date/time formatted string used by the DB into a GregorianCalendar object.
	 * @param formatted the formatted string from the DB.
	 * @param output the GregorianCalendar object to be used to represent date/time in the application.
	 */
	private static void FormattedDateToGregorian(String formatted, GregorianCalendar output)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			java.util.Date dateTime = sdf.parse(formatted);
			output.setTime(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to add a new entry into the DB representing an Appointment for a given AppointmentBook. Each AppointmentBook has its own table in the DB.
	 * Each appointment is then stored in it's respective AppointmentBook. THIS METHOD IS BUGGY: it seems that the function doesn't successfully add
	 * Appointments to tables which have punctuation in (e.g '.' or ',' such as [Test.ics] table names). Needs further development to isolate the problem.
	 * @param tableName the name of the table representing the relevant AppointmentBook.
	 * @param appointment the Appointment object to be stored in the DB.
	 */
	public static void AddAppointmentToDatabase(String tableName, Appointment appointment)
	{
		/*int startDay = appointment.getStartDateTime().get(Calendar.DAY_OF_MONTH);
		int startMonth = appointment.getStartDateTime().get(Calendar.MONTH);
		int startYear = appointment.getStartDateTime().get(Calendar.YEAR);
		int endDay = appointment.getEndDateTime().get(Calendar.DAY_OF_MONTH);			//DEPRECATED
		int endMonth = appointment.getEndDateTime().get(Calendar.MONTH);
		int endYear = appointment.getEndDateTime().get(Calendar.YEAR);*/
		
		String startDate = dateFormatter(appointment.getStartDateTime());
		String endDate = dateFormatter(appointment.getEndDateTime());
		
		String eventTitle = appointment.getEventTitle();
		String description = appointment.getEventDescription();
		String location = appointment.getEventLocation();
		String query = String.format("INSERT INTO \""+tableName+"\" (dateTimeFrom, dateTimeTo, eventTitle, description, location) VALUES (\"%s\", \"%s\", \"%s\", \"%s\", \"%s\");", startDate, endDate, eventTitle, description, location);
		
		MakeRequest(query);
		
	}
	
	/**
	 * Method to remove a given Appointment record from an AppointmentBook table. THIS METHOD IS BUGGY: I'm not certain if finding the relevant Appointment record should
	 * be done just using the Appointment objects eventTitle field. What if there is a repeated event with the same name but different times???
	 * @param tableName the AppointmentBook's name representing the table in question.
	 * @param appointment the Appointment object to remove.
	 */
	public static void RemoveAppointmentFromDatabase(String tableName, Appointment appointment)
	{
		String query = "DELETE FROM "+tableName+" WHERE eventTitle == \""+appointment.getEventTitle()+"\";";
		
		MakeRequest(query);
		
	}
	
	/**
	 * Method to fetch all Appointment records stored in the DB for a given AppointmentBook.
	 * @param BookName the AppointmentBook to get all the stored Appointment records for.
	 * @return an ArrayList of Appointment objects.
	 */
	public static ArrayList<Appointment> GetAllAppointmentsFromDatabase(String BookName)//, Appointment appointment)
	{
		String query = "SELECT * FROM "+BookName+";";
		ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
		MakeRequestWithOutput(query);
		try
		{
			try 
			{
				
				while(resultSet.next())
				{
					Appointment a = new Appointment();
					
					GregorianCalendar tempDateTime = new GregorianCalendar();
					
					FormattedDateToGregorian(resultSet.getString(1), tempDateTime);
					a.setStartDateTime(tempDateTime);
					
					FormattedDateToGregorian(resultSet.getString(2), tempDateTime);
					a.setEndDateTime(tempDateTime);
					
					a.setEventTitle(resultSet.getString(3));
					
					a.setEventDescription(resultSet.getString(4));
					
					a.setEventLocation(resultSet.getString(5));
					appointmentList.add(a);
				}
				
			}
			finally
			{
				if(resultSet != null)
					resultSet.close();
				if(statement != null)
					statement.close();
				if(dbConnection != null)
					dbConnection.close();
			}
		} catch (SQLException e) {
			System.out.println("GetAllAppointmentsFromDatabase(String tableName)");
			e.printStackTrace();
		}
		return appointmentList;
	
	}
	
	/**
	 * Method to return a specific Appointment record stored in the DB.
	 * @param BookName the name of the AppointmentBook table to search in.
	 * @param appointment the Appointment object to find records for.
	 */
	public static void GetSpecificAppointmentFromDatabase(String BookName, Appointment appointment)
	{
		String startDate = dateFormatter(appointment.getStartDateTime());
		String endDate = dateFormatter(appointment.getEndDateTime());
		
		String eventTitle = appointment.getEventTitle();
		
		String query = String.format("SELECT * FROM %s WHERE dateTimeFrom = \"%s\" AND dateTimeTo = \"%s\" AND eventTitle = \"%s\";", BookName, startDate, endDate, eventTitle);
		System.out.println(query);
		MakeRequestWithOutput(query);
		try
		{
			try 
			{
	
				while(resultSet.next())
				{
					System.out.println("Item exists.");
				}
				
			}
			finally
			{
				if(resultSet != null)
					resultSet.close();
				if(statement != null)
					statement.close();
				if(dbConnection != null)
					dbConnection.close();
			}
		} catch (SQLException e) {
			System.out.println("CheckIfAppointmentBookExistsOnDatabase(String tableName)");
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to return a boolean value determining if an AppointmentBook is already represented on the DB by its appointmentBookName.
	 * @param BookName the appointmentBookName to search for.
	 * @return true is the AppointmentBook object is represented on the DB, otherwise false.
	 */
	public static boolean CheckIfAppointmentBookExistsOnDatabase(String BookName)
	{
		String query = "SELECT * FROM sqlite_master;";
		MakeRequestWithOutput(query);
		boolean ret = false;
		try
		{
			try 
			{
	
				while(resultSet.next())
				{
					//System.out.println("Input: "+BookName+ " CHECK: "+resultSet.getString(2));
					if(resultSet.getString(2).contentEquals(BookName))
						ret = true;
					//System.out.println(ret);
				}
				
			}
			finally
			{
				if(resultSet != null)
					resultSet.close();
				if(statement != null)
					statement.close();
				if(dbConnection != null)
					dbConnection.close();
			}
		} catch (SQLException e) {
			System.out.println("CheckIfAppointmentBookExistsOnDatabase(String tableName)");
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Method to create a new table for an AppointmentBook using its appointmentBookName string to label it.
	 * @param tableName the desired name of the AppointmentBook.
	 */
	public static void SetupNewAppointmentBookForDatabase(String tableName)
	{
		//SQLi possible here :/. Dont have time to fix now.
		String query = "CREATE TABLE IF NOT EXISTS \""+ tableName +"\"(dateTimeFrom TEXT NOT NULL, dateTimeTo TEXT, eventTitle VARCHAR NOT NULL, description VARCHAR, location VARCHAR);";
		
		MakeRequest(query);
		
	}
	
	/**
	 * Method to remove a given AppointmentBook table from the DB.
	 * @param tableName the appointmentBookName used to represent the table.
	 */
	public static void RemoveAppointmentBookFromDatabase(String tableName)
	{
		String query = "DROP TABLE \"main\".\""+tableName+"\";";
		
		MakeRequest(query);
		
	}

	/**
	 * Method to return all AppointmentBooks currently represented by tables in the DB.
	 * @param bookNames the ArrayList to return the results of the query in.
	 */
	public static void GetAllAppointmentBooks(ArrayList<String> bookNames)
	{
		String query = "SELECT * FROM sqlite_master;";
		MakeRequestWithOutput(query);
		
		try
		{
			try 
			{
	
				while(resultSet.next())
				{
					bookNames.add(resultSet.getString(2));
				}
				
			}
			finally
			{
				if(statement != null)
					statement.close();
				if(resultSet != null)
					resultSet.close();
				if(dbConnection != null)
					dbConnection.close();
			}
		} catch (SQLException e) {
			System.out.println("GetAllAppointmentBooks(ArrayList<AppointmentBook> booksList)");
			e.printStackTrace();
		}
	}
}
