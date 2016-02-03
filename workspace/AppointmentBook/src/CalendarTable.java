import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class CalendarTable {
	
	private DefaultTableModel calendarModel;
	JTable jtbl;
	int month, year;
	public CalendarTable(int month, int year, AppointmentBook thisBook)
	{
		this.month = month;
		this.year = year;
		int daysInMonth = determineDaysInMonth();
		
		ArrayList<Appointment> thisMonthsAppointments = findAppointmentsForMonth(thisBook);
		drawTable(daysInMonth);//, thisMonthsAppointments);
	}
	
	private int determineDaysInMonth()
	{
		GregorianCalendar tmpCal = new GregorianCalendar(year, month, 1);
		int noOfDays = tmpCal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return noOfDays;
	}
	
	private String determineStartDay()
	{
		String startDay = "";
		
		Calendar tmpCal = new GregorianCalendar(year, month, 1);
		int dayOfWeek = tmpCal.get(Calendar.DAY_OF_WEEK);
		
		switch(dayOfWeek)
		{
			case Calendar.MONDAY:
			{
				startDay = "Monday";
				break;
			}
			case Calendar.TUESDAY:
			{
				startDay = "Tuesday";
				break;
			}
			case Calendar.WEDNESDAY:
			{
				startDay = "Wednesday";
				break;
			}
			case Calendar.THURSDAY:
			{
				startDay = "Thursday";
				break;
			}
			case Calendar.FRIDAY:
			{
				startDay = "Friday";
				break;
			}
			case Calendar.SATURDAY:
			{
				startDay = "Saturday";
				break;
			}
			case Calendar.SUNDAY:
			{
				startDay = "Sunday";
				break;
			}
		}
		
		System.out.println(startDay);
		return startDay;
	}
	
	private ArrayList<Appointment> findAppointmentsForMonth(AppointmentBook thisBook)
	{
		ArrayList<Appointment> thisMonthsAppointments = new ArrayList<Appointment>();
		Calendar tmpCal = new GregorianCalendar(year, month, 1);
		Calendar startOfMonth = tmpCal;
		Calendar endOfMonth = new GregorianCalendar(year, month, tmpCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		System.out.println(thisBook.appointmentList.size());
		
		for(int i = 0; i < thisBook.appointmentList.size(); i++)
		{
			boolean afterOrEqualToStartOfMonth = (thisBook.appointmentList.get(i).getStartDateTime().after(startOfMonth)) || (thisBook.appointmentList.get(i).getStartDateTime().equals(startOfMonth));
			boolean beforeOrEqualToEndOfMonth = (thisBook.appointmentList.get(i).getStartDateTime().before(endOfMonth)) || (thisBook.appointmentList.get(i).getStartDateTime().equals(endOfMonth));
			if((afterOrEqualToStartOfMonth) && (beforeOrEqualToEndOfMonth))
				thisMonthsAppointments.add(thisBook.appointmentList.get(i));
		}
		
		return thisMonthsAppointments;
	}
	
	private void drawTable(int daysInMonth)//, ArrayList<Appointment> thisMonthsAppointments)
	{
		String[] columnNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
		
		calendarModel = new DefaultTableModel(columnNames, 6);
		jtbl = new JTable(calendarModel);
		
		calendarModel.setRowCount(7);
		
		int startpoint = 0;
		if(!(determineStartDay().equalsIgnoreCase("Monday")))
		{
			String day = determineStartDay();
			switch(day)
			{
				case "Monday":
				{
					startpoint = 0;
					break;
				}
				case "Tuesday":
				{
					startpoint = 1;
					break;
				}
				case "Wednesday":
				{
					startpoint = 2;
					break;
				}
				case "Thursday":
				{
					startpoint = 3;
					break;
				}
				case "Friday":
				{
					startpoint = 4;
					break;
				}
				case "Saturday":
				{
					startpoint = 5;
					break;
				}
				case "Sunday":
				{
					startpoint = 6;
					break;
				}
			}
		}	
		
		//System.out.println("Startpoint: "+startpoint);
		
		int k = 0;
		
		for(int i = startpoint; i < 7; i++)
		{
			k++;
			jtbl.setValueAt(k, 0, i);
		}
		
		for(int i = 1; i < 7; i++)
		{
			for(int j = 0; j <= 6; j++)
			{
				k++;
				if(k > daysInMonth)
					break;
				
				jtbl.setValueAt(k,  i, j);
			}
			if(k > daysInMonth)
				break;
		}
		
	}
}