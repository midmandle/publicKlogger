import java.util.ArrayList;
import java.util.Calendar;

public class AppointmentBook {
	int NOTFOUND = 0;
	int FOUND = 1;
	
	String appointmentBookName;
	
	ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
	
	public AppointmentBook(String appointmentBookName)
	{
		this.appointmentBookName = appointmentBookName;
		boolean exists = DatabaseCommunicator.CheckIfAppointmentBookExistsOnDatabase(appointmentBookName);
		
		if(exists)
		{
			intialiseAppointmentBookFromDatabase();
		}
		else
		{
			System.out.println("Ping");
			DatabaseCommunicator.SetupNewAppointmentBookForDatabase(appointmentBookName);
		}
			
	
	}
	
	public int add(Appointment newAppointment)
	{
		if(isInBook(newAppointment))
		{
			System.out.println("ALREADY EXISTS");
			return FOUND;//TODO: Throw exception: "ITEM ALREADY EXISTS"
		}
		else
		{
			appointmentList.add(newAppointment);
			
			DatabaseCommunicator.AddAppointmentToDatabase(appointmentBookName, newAppointment);
			return NOTFOUND;
		}
	}
	
	public void intialiseAppointmentBookFromDatabase()
	{
		appointmentList = DatabaseCommunicator.GetAllAppointmentsFromDatabase(appointmentBookName);
	}
	
	public ArrayList<Appointment> getAllAppointments()
	{
		return appointmentList;
	}
	
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
	
	public int find(Appointment appointmentToFind)
	{
		for(int i = 0; i < appointmentList.size(); i++)
			if(appointmentList.get(i).toString().contentEquals(appointmentToFind.toString())) //TODO: Needs to be more effective.
				return FOUND;
		return NOTFOUND;
	}
	
	public Appointment findAndReturn(String appointmentName)
	{
		for(int i = 0; i < appointmentList.size(); i++)
			if(appointmentList.get(i).toString().contentEquals(appointmentName)) //TODO: Needs to be more effective.
			{
				System.out.println("CHECK");
				return appointmentList.get(i);
			}
		return null;
	}
	
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
	
	public boolean isInBook(Appointment appointmentToCheck)
	{
		if(find(appointmentToCheck) > 0)
		{
			System.out.println("CHECK");
			return true;
		}
		return false;
	}
	
	public void SetAppointmentBookName(String name)
	{
		appointmentBookName = name;
	}
	
	public void saveAppointmentsToDatabase()
	{
		for(int i = 0; i < appointmentList.size(); i++)
		{
			DatabaseCommunicator.AddAppointmentToDatabase(appointmentBookName, appointmentList.get(i));
		}
	}
	
	
}
