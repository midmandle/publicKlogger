import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import com.toedter.calendar.JCalendar;


public class CalendarPane extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	AppointmentBook thisBook;
	
	public CalendarPane(final AppointmentBook thisBook)
	{
		setName("CalendarPane");
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.thisBook = thisBook;
		
		final JPanel cardsPanel = new JPanel();
		
		MonthViewPanel calendarView = new MonthViewPanel(thisBook, cardsPanel);
		
		AddEditAppointmentPanel addEditAppointmentView = new AddEditAppointmentPanel(thisBook, this);
		
		
		cardsPanel.setLayout(new CardLayout());
		cardsPanel.add(calendarView, "Calendar View");
		cardsPanel.add(addEditAppointmentView, "Add Appointment View");
		
		JButton buttonExitCalendarPane = new JButton("Close");
		buttonExitCalendarPane.setAlignmentX(CENTER_ALIGNMENT);
		
		buttonExitCalendarPane.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	closeTab();
            }
        });
		add(cardsPanel);
		add(buttonExitCalendarPane);
	}
	
	
	

	public void closeTab()
	{
		//Do stuff to close this tab
		JTabbedPane jtp = (JTabbedPane)getParent();
		int i = jtp.getSelectedIndex();
		jtp.remove(i);
	}
	
}
