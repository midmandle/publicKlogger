import java.util.ArrayList;

public class AppointmentBook {
	int NOTFOUND = 0;
	int FOUND = 1;
	
	ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
	
	public AppointmentBook()
	{
		
	}
	
	public void add(Appointment newAppointment)
	{
		if(isInBook(newAppointment))
			return; //TODO: Throw exception: "ITEM ALREADY EXISTS"
		else
			appointmentList.add(newAppointment);
	}
	
	public ArrayList<Appointment> getAllAppointments()
	{
		return appointmentList;
	}
	
	public void showAllAppointments()
	{
		for(int i = 0; i < appointmentList.size(); i++)
			System.out.println(appointmentList.get(i).toString());
	}
	
	public int find(Appointment appointmentToFind)
	{
		for(int i = 0; i < appointmentList.size(); i++)
			if(appointmentList.get(i).eventTitle == appointmentToFind.eventTitle)
				return FOUND;
		return NOTFOUND;
	}
	
	public void remove(Appointment appointmentToRemove)
	{
		if(isInBook(appointmentToRemove))
			appointmentList.remove(appointmentToRemove);
		else
			return;//TODO: Throw exception: "ITEM DOESNT EXIST"
	}
	
	public boolean isInBook(Appointment appointmentToCheck)
	{
		if(find(appointmentToCheck) > 0)
			return true;
		return false;
	}
}
