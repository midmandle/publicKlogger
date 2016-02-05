import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * CalendarPane is the class which forms the outer container for the components required to manipulate a specific AppointmentBook.
 * @author 14061121
 *
 */
public class CalendarPane extends JPanel{
	private static final long serialVersionUID = 1L;
	
	AppointmentBook thisBook;
	
	/**
	 * Constructor which specifies the JPanel layout (CardLayout) and implementing a "Close" button to leave the current JTab.
	 * @param thisBook the AppointmentBook relevant to this component.
	 */
	public CalendarPane(final AppointmentBook thisBook)
	{
		setName("CalendarPane");
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.thisBook = thisBook;
		
		final JPanel cardsPanel = new JPanel();
	 
		MonthViewPanel monthView = new MonthViewPanel(thisBook, cardsPanel);
		monthView.setName("MonthView");
		AddEditAppointmentPanel addEditAppointmentView = new AddEditAppointmentPanel(thisBook, this);
		//addEditAppointmentView.setName("AddEditView");

		cardsPanel.setLayout(new CardLayout());
		cardsPanel.add(monthView, "MonthView");
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
	
	/**
	 * Method to implement the functionality of the button that closes the current tab.
	 */
	public void closeTab()
	{
		//Do stuff to close this tab
		JTabbedPane jtp = (JTabbedPane)getParent();
		int i = jtp.getSelectedIndex();
		jtp.remove(i);
	}
	
}
