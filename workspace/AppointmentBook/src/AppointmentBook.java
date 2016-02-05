import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The AppointmentBook class is a wrapper class for the Appointment class.
 * It acts as a management class for multiple appointments grouped together.
 * @author 14061121
 *
 */

public class AppointmentBook {
	int NOTFOUND = 0;
	int FOUND = 1;
	int NO_TIME_CLASH = 0;
	int TIME_CLASH = -1;
	
	String appointmentBookName;
	
	ArrayList<Appointment> appointmentList = null;
	
	/**
	 * Constructor for the AppointmentBook object. Requires a name for the AppointmentBook
	 * so that in the event of multiple AppointmentBook objects they can be logically
	 * distinguished from one another by the user.
	 * @param appointmentBookName The logical name of the newly created AppointmentBook object.
	 */
	public AppointmentBook(String appointmentBookName)
	{
		
		this.appointmentBookName = appointmentBookName;
		appointmentList = new ArrayList<Appointment>();
		
		boolean exists = DatabaseCommunicator.CheckIfAppointmentBookExistsOnDatabase(appointmentBookName);
		
		if(exists)
		{
			intialiseAppointmentBookFromDatabase();
		}
		else
		{
			//System.out.println("Ping");
			DatabaseCommunicator.SetupNewAppointmentBookForDatabase(appointmentBookName);
		}
	}
	
	/**
	 * Adds a new Appointment object to the appointmentList maintained by the AppointmentBook class.
	 * @param newAppointment The new Appointment object to add.
	 * @return Returns an integer value depending on if the new Appointment object was successfully added to the AppointmentBook.
	 * If the Appointment to add already exists then a positive value will be returned. If the new Appointment clashes with an exiting
	 * appointment then a -1 negative value will be returned. If appointment is added successfully then a 0 is returned.
	 */
	public int add(Appointment newAppointment)
	{
		int ret = 0;
		if(isInBook(newAppointment)) //Comment out these checks to conduct assignment testing code.
		{
			System.out.println("ALREADY EXISTS");
			return FOUND; 							
		}
		if(checkForTimeClash(newAppointment) < 0) //Comment out these checks to conduct assignment testing code.
				return TIME_CLASH;
			
		appointmentList.add(newAppointment);
		
		DatabaseCommunicator.AddAppointmentToDatabase(appointmentBookName, newAppointment);
		return ret;
	}
	
	/**
	 * intialiseAppointmentBookFromDatabase() takes the logical name of the AppointmentBook object and uses it to initialise the current AppointmentBook object's appointmentList.
	 */
	public void intialiseAppointmentBookFromDatabase()
	{
		appointmentList = DatabaseCommunicator.GetAllAppointmentsFromDatabase(appointmentBookName);
	}
	
	/**
	 * getAllAppointments() returns all the Appointment objects for the AppointmentBook object as an ArrayList.
	 * @return an ArrayList<Appointment> object containing all the Appointments for the AppointmentBook.
	 */
	public ArrayList<Appointment> getAllAppointments()
	{
		return appointmentList;
	}
	
	/**
	 * showAllAppointments() prints all Appointment objects managed by the AppointmentBook object to the System.out.*.
	 */
	public void showAllAppointments()
	{
		System.out.println(appointmentList.size()+" appointments:");
		for(int i = 0; i < appointmentList.size(); i++)
		{		
			int startDay = 0;
			int startMonth = 0;
			int startYear = 0;
			
			int endDay = 0;
			int endMonth = 0;
			int endYear = 0;
			
			String title = null;
			
			startDay = appointmentList.get(i).getStartDateTime().get(Calendar.DAY_OF_MONTH);
			startMonth = appointmentList.get(i).getStartDateTime().get(Calendar.MONTH);
			startYear = appointmentList.get(i).getStartDateTime().get(Calendar.YEAR);
			
			endDay = appointmentList.get(i).getEndDateTime().get(Calendar.DAY_OF_MONTH);
			endMonth = appointmentList.get(i).getEndDateTime().get(Calendar.MONTH);
			endYear = appointmentList.get(i).getEndDateTime().get(Calendar.YEAR);
			
			title = appointmentList.get(i).getEventTitle();
			
			String output = String.format("%s Starts: %d/%d/%d Ends: %d/%d/%d", title, startDay, startMonth, startYear, endDay, endMonth, endYear);
			
			System.out.println(output);
			
		}
			
	}
	
	/**
	 * find(Appointment appointmentToFind) searches through the AppointmentBook objects ArrayList of Appointment objects for one whose toString() output matches that of the
	 * supplied appointmentToFind's toString() output. This is not effective. Needs heavy reconsideration.
	 * @param appointmentToFind the Appointment object to find.
	 * @return 1 if found and 0 if not found.
	 */
	public int find(Appointment appointmentToFind)
	{
		for(int i = 0; i < appointmentList.size(); i++)
			if(appointmentList.get(i).getEventTitle().compareTo(appointmentToFind.getEventTitle()) == 0) //TODO: Needs to be more effective.
				return FOUND;
		return NOTFOUND;
	}
	
	/**
	 * findAndReturn(String appointmentName) similar to find(Appointment appointmentToFind) except it will return the Appointment object if found. This is not effective. Needs heavy reconsideration.
	 * @param appointmentName the name of the Appointment object to find.
	 * @return returns the Appointment which has a matching name to appointmentName parameter or null if not found.
	 */
	public Appointment findAndReturn(String appointmentName)
	{
		for(int i = 0; i < appointmentList.size(); i++)
			if(appointmentList.get(i).toString().compareTo(appointmentName) == 0) //TODO: Needs to be more effective.
			{
				//System.out.println("CHECK");
				return appointmentList.get(i);
			}
		return null;
	}
	
	/**
	 * Checks if the Appointment object that wants to be removed exists in the current AppointmentBook and removes it if it does.
	 * @param appointmentToRemove the Appointment object to remove.
	 */
	public void remove(Appointment appointmentToRemove)
	{
		if(isInBook(appointmentToRemove))
		{
			appointmentList.remove(appointmentToRemove);
			DatabaseCommunicator.RemoveAppointmentFromDatabase(appointmentBookName, appointmentToRemove);
		}
		else
			return;//TODO: Throw exception: "ITEM DOESNT EXIST"
	}
	
	/**
	 * Method to check if an Appointment object exists in the AppointmentBook.
	 * @param appointmentToCheck the Appointment object to check for.
	 * @return true if the item does exist in the AppointmentBook otherwise, returns false;
	 */
	public boolean isInBook(Appointment appointmentToCheck)
	{
		if(find(appointmentToCheck) > 0)
		{
			//System.out.println("CHECK");
			return true;
		}
		return false;
	}
	
	/**
	 * Check to see if the supplied Appointment object's times clash with an existing Appointment object.
	 * @param appointmentToCheck the Appointment to check against the existing Appointments in the AppointmenBook.
	 * @return -1 if the times clash or 0 if not.
	 */
	public int checkForTimeClash(Appointment appointmentToCheck)
	{
		for(int i = 0; i < appointmentList.size(); i++)
		{
			GregorianCalendar originalStart = appointmentList.get(i).getStartDateTime();
			GregorianCalendar originalEnds = appointmentList.get(i).getEndDateTime();
			
			GregorianCalendar toCheckStart = appointmentToCheck.getStartDateTime();
			GregorianCalendar toCheckEnds = appointmentToCheck.getEndDateTime();
			
			//Starts in the middle of an existing appointment.
			boolean startOverlap = (toCheckStart.after(originalStart) && toCheckStart.before(originalEnds));
			//Ends in the middle of an existing appointment.
			boolean endOverlap = (toCheckEnds.after(originalStart) && toCheckEnds.before(originalEnds));
			//Starts or finishes at the same time as an existing appointment.
			boolean equal = (toCheckStart.compareTo(originalStart) == 0 || toCheckEnds.compareTo(originalEnds)== 0);
			if(startOverlap || endOverlap || equal)
				return TIME_CLASH;
		}
		return NO_TIME_CLASH;
	}
	
	/**
	 * Set the logical name of the AppointmentBook.
	 * @param name the logical name to set.
	 */
	public void SetAppointmentBookName(String name)
	{
		appointmentBookName = name;
	}
	
	/**
	 * Saves the AppointmeBook's appointmentList to the DB by force. Rarely used as the updating of the DB should happen seamlessly on the go (in reality it is far more buggy than this and needs revising).
	 */
	public void saveAppointmentsToDatabase()
	{
		for(int i = 0; i < appointmentList.size(); i++)
		{
			DatabaseCommunicator.AddAppointmentToDatabase(appointmentBookName, appointmentList.get(i));
		}
	}
	
	
}
