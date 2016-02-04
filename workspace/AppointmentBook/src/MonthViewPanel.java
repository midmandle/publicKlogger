import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.toedter.calendar.JYearChooser;


public class MonthViewPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	GregorianCalendar todayDate = new GregorianCalendar();
	int year = todayDate.get(Calendar.YEAR);
	int month = todayDate.get(Calendar.MONTH);
	CalendarTable calT;
	AppointmentBook thisBook;
	JPanel parent;
	
	public MonthViewPanel(AppointmentBook thisBook, JPanel parent)
	{
		setName("MonthlyViewPanel");
		this.parent = parent;
		this.thisBook = thisBook;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		final JYearChooser jyc = new JYearChooser();
		
		//MonthForward
		JButton monthForward = new JButton(">>");
		monthForward.setAlignmentX(RIGHT_ALIGNMENT);
		
		//END
		
		//MonthBackwards
		JButton monthBackwards = new JButton("<<");
		monthBackwards.setAlignmentX(LEFT_ALIGNMENT);
		//END
		
		//CalendarOptions
		final JPanel calendarOptions = new JPanel();
		calendarOptions.add(monthBackwards);
		calendarOptions.add(jyc);
		calendarOptions.add(monthForward);
		//END
		
		
		//ACTION LISTENERS
		
		monthBackwards.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(month == 0)
            		month = 12;
            	else
            		month--;
            	
            	updateCalendar();
            	add(calendarOptions);
            }
        });
		
		monthForward.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(month == 12)
            		month = 0;
            	else
            		month++;
            	
            	updateCalendar();
            	add(calendarOptions);
            }
        });
		
		jyc.addPropertyChangeListener("year", new PropertyChangeListener() {

		    @Override
		    public void propertyChange(PropertyChangeEvent e) {
		        year = jyc.getValue(); 
		        updateCalendar();
		        add(calendarOptions);
		    }
		});
		
		//END
		
		updateCalendar();	
		add(calendarOptions);
	}
	
	private void updateCalendar()
	{
		removeAll();
		calT = new CalendarTable(month, year, thisBook, parent);
		add(new JScrollPane(calT.jtbl));
		JLabel monthLabel;
		switch(month)
		{
			case 0:
			{
				monthLabel = new JLabel("Jan");
				break;
			}
			case 1:
			{
				monthLabel = new JLabel("Feb");
				break;
			}
			case 2:
			{
				monthLabel = new JLabel("Mar");
				break;
			}
			case 3:
			{
				monthLabel = new JLabel("April");
				break;
			}
			case 4:
			{
				monthLabel = new JLabel("May");
				break;
			}
			case 5:
			{
				monthLabel = new JLabel("June");
				break;
			}
			case 6:
			{
				monthLabel = new JLabel("July");
				break;
			}
			case 7:
			{
				monthLabel = new JLabel("Aug");
				break;
			}
			case 8:
			{
				monthLabel = new JLabel("Sep");
				break;
			}
			case 9:
			{
				monthLabel = new JLabel("Oct");
				break;
			}
			case 10:
			{
				monthLabel = new JLabel("Nov");
				break;
			}
			case 11:
			{
				monthLabel = new JLabel("Dec");
				break;
			}
			default:
			{
				monthLabel = new JLabel("Jan");
				break;
			}
		}
		monthLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(monthLabel);
		this.revalidate();
	}
}
