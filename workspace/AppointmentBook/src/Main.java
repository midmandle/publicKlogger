import java.util.GregorianCalendar;

public class Main {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				MainFrame f = new MainFrame();

				//!!!!___TESTING CODE. DO NOT RUN UNLESS YOU HAVE MODIFIED THE RELEVANT AREAS IN AppointmentBook CLASS.___!!!//
				/*
				Appointment a1 = new Appointment(new GregorianCalendar(2015, 8+1, 14, 10,30), new GregorianCalendar(2015, 10, 14,11,30), "Dentist");
				Appointment a2 = new Appointment(new GregorianCalendar(2015, 8+1, 20,9,00), new GregorianCalendar(2015, 10, 20,10,10), "OOWP Lecture");
				Appointment a3 = new Appointment(new GregorianCalendar(2015, 8+1, 21,14,00), new GregorianCalendar(2015, 10, 21,16,00), "Tutorial");
				AppointmentBook appBook = new AppointmentBook("Test");
				
				appBook.add(a1);
				
				appBook.add(a2);
		
				appBook.add(a3);
				
				appBook.showAllAppointments();
				
				appBook.remove(a1);
				
				appBook.showAllAppointments();
				
				System.out.println("Find Test: "+ appBook.isInBook(a2));
				System.out.println("Find Test: "+ appBook.isInBook(a1));
				*/
			}
		});
		
	}

}
