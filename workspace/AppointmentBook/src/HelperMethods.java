import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

/**
 * Static class implementing various methods which dont fit anywhere else in the code structure at present. If there was further development of any
 * of these methods I would move them into their own class.
 * @author 14061121
 *
 */
public class HelperMethods {
	
	//Save current appointmentBooks as CSV
	
	/**
	 * Saves the currently tracked AppointmentBook object's Appointments to CSV file.
	 * @param location the desired location of the CSV save file.
	 * @param appointmentBook the AppointmentBook object whose contents will be saved.
	 */
	public static void SaveBooksToCSV(String location, AppointmentBook appointmentBook)
	{
		try
		{
			File file = new File(location);
			FileWriter writer = new FileWriter(file);
			
			for(int i = 0; i < appointmentBook.appointmentList.size(); i++)
			{
				writer.append(appointmentBook.appointmentList.get(i).getEventTitle());
				writer.append(',');
				writer.append((CharSequence) appointmentBook.appointmentList.get(i).getStartDateTime().getTime().toString());
				writer.append(',');
				writer.append((CharSequence) appointmentBook.appointmentList.get(i).getEndDateTime().getTime().toString());
				writer.append(',');
				writer.append(appointmentBook.appointmentList.get(i).getEventDescription());
				writer.append(',');
				writer.append(appointmentBook.appointmentList.get(i).getEventLocation());
				writer.append(',');
				writer.append('\n');
			}
			
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	//Fetch current appointmentBooks from CSV
	/**
	 * Loads values from a CSV file into their own AppointmentBook to be tracked by the application.
	 * @param location the location of the CSV file to load from.
	 * @param booksList the ArrayList of AppointmentBook objects to add the newly generated AppointmentBook to.
	 */
	public static void FetchBooksFromCSV(String location, ArrayList<AppointmentBook> booksList)
	{
		File file = new File(location);
		BufferedReader br = null;
		String line = "";
		String splitOn = ",";
		
		String filename;
		int i = location.lastIndexOf("/");
		
		filename = location.substring(i+1, location.length());
		
		System.out.println(filename);
		
		AppointmentBook appointmentBook = new AppointmentBook(filename);
		booksList.add(appointmentBook);
		
		try
		{
			br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null)
			{
				String[] appointment = line.split(splitOn);
				Appointment appointmentAdder = new Appointment();
				
				DateFormat df = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
				Date dStart = null;
				Date dFinish = null;
				try {
					dStart = df.parse(appointment[1]);
					dFinish = df.parse(appointment[2]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(dStart);
				GregorianCalendar gregStart = (GregorianCalendar) cal;
				cal.setTime(dFinish);
				GregorianCalendar gregFinish = (GregorianCalendar) cal;
				
				appointmentAdder.setEventTitle(appointment[0]);
				appointmentAdder.setStartDateTime(gregStart);
				appointmentAdder.setEndDateTime(gregFinish);
				if(appointment.length > 3)
				{
					appointmentAdder.setEventDescription(appointment[3]);
					if(appointment.length > 4)
						appointmentAdder.setEventLocation(appointment[4]);
				}
				
				
				appointmentBook.add(appointmentAdder);
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(br != null)
			{
				try{
					br.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//Fetch current Books from Database;
	/**
	 * Method to initialise any AppointmentBook object held in the DB into the application.
	 * @param booksList the ArrayList of AppointmentBook object to add the newly initialised AppointmentBook objects into.
	 */
	public static void InitialiseBooksFromDatabase(ArrayList<AppointmentBook> booksList)
	{
		ArrayList<String> bookNames = new ArrayList<String>();
		DatabaseCommunicator.GetAllAppointmentBooks(bookNames);
		
		for(int i = 0; i < bookNames.size(); i++)
		{
			booksList.add(new AppointmentBook(bookNames.get(i)));
		}
		
		for(int i = 0; i < booksList.size(); i++)
		{
			booksList.get(i).intialiseAppointmentBookFromDatabase();
		}
	}
	
	//Whatever the current state of the AppointmentBook selected from the comboBox on the Welcome tab it will be saved in that form to the DB.
	/**
	 * Whatever the current state of the AppointmentBook selected from the comboBox on the Welcome tab it will be saved in that form to the DB.
	 * @param book the AppointmentBook object to save to the DB.
	 */
	public static void ForceSaveToDB(AppointmentBook book)
	{
		DatabaseCommunicator.RemoveAppointmentBookFromDatabase(book.appointmentBookName);
		DatabaseCommunicator.SetupNewAppointmentBookForDatabase(book.appointmentBookName);
		book.saveAppointmentsToDatabase();
	}
	
	/**
	 * Method to export the selected AppointmentBook to an ICS file. Uses ical4j library.
	 * @param book the AppointmentBook to export.
	 * @param location the desired location of the ICS file.
	 */
	public static void ExportAsICS(AppointmentBook book, String location)
	{
		net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("Europe/London");
		VTimeZone tz = timezone.getVTimeZone();
		
		for(int i = 0; i < book.appointmentList.size(); i++)
		{
			DateTime start = new DateTime(book.appointmentList.get(i).getStartDateTime().getTime());
			DateTime end = new DateTime(book.appointmentList.get(i).getEndDateTime().getTime());
			VEvent appointment = new VEvent(start, end, book.appointmentList.get(i).getEventTitle());
			
			appointment.getProperties().add(tz.getTimeZoneId());
			try {
				UidGenerator ug = new UidGenerator("UidGen");
				Uid uid = ug.generateUid();
				appointment.getProperties().add(uid);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			calendar.getComponents().add(appointment);
		}
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(location);
			CalendarOutputter outputter = new CalendarOutputter();
			outputter.output(calendar, fout);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method to import the selected ICS file into the AppointmentBook ArrayList. Uses ical4j library.
	 * @param booksList the ArrayList of AppointmentBook objects to add the newly generated AppointmentBook to.
	 * @param location the location of the ICS file to import.
	 */
	public static void ImportAsICS(ArrayList<AppointmentBook> booksList, String location)
	{
		String filename;
		int i = location.lastIndexOf("/");
		
		filename = location.substring(i+1, location.length());
		
		System.out.println(filename);
		
		AppointmentBook appointmentBook = new AppointmentBook(filename);
		booksList.add(appointmentBook);
		
		FileInputStream fin;
		try {
			fin = new FileInputStream(location);
			CalendarBuilder builder = new CalendarBuilder();
			net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);
			
			DateTime startTime = null;
			DateTime endTime = null;
			String eventTitle = null;
			if(calendar != null)
			{
				for (Iterator j = (Iterator) calendar.getComponents().iterator(); j.hasNext();) {
					net.fortuna.ical4j.model.Component component = (net.fortuna.ical4j.model.Component) j.next();
				      System.out.println("Component [" + component.getName() + "]");

				      for (Iterator k = ((net.fortuna.ical4j.model.Component) component).getProperties().iterator(); k.hasNext();) {
				          Property property = (Property) k.next();
				          System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");
				          switch(property.getName())
				          {
				          	case "DTSTART":
				          	{
				          		try {
									startTime = new DateTime(property.getValue());
									
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				          		break;
				          	}
				          	case "DTEND":
				          	{
				          		try {
									endTime = new DateTime(property.getValue());
									
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				          		break;
				          	}
				          	case "SUMMARY":
				          	{
								eventTitle = property.getValue();
								
								break;
				          	}
				          }
				      }
				      if(startTime == null || endTime == null || eventTitle == null)
			        	  continue;
			          else
			          {
			        	  GregorianCalendar startGreg = new GregorianCalendar();
			        	  startGreg.setTime(startTime);
			        	  
			        	  GregorianCalendar endGreg = new GregorianCalendar();
			        	  endGreg.setTime(endTime);
			        	  
			        	  Appointment tempAppointment = new Appointment(startGreg, endGreg, eventTitle);
			        	  
			        	  System.out.println(tempAppointment.toString());
			        	  
			        	  appointmentBook.add(tempAppointment);
			        	  
			          }
				  }//for;
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		
	}
}
