import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class SelectPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JComboBox<String> bookSelectorCombo = new JComboBox<String>();
	ArrayList<AppointmentBook> booksList;
	
	public SelectPanel(final ArrayList<AppointmentBook> booksList)
	{
		super();
		this.booksList = booksList;
		
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel pleaseChoose = new JLabel("Please choose your appointment book:");
		pleaseChoose.setAlignmentX(CENTER_ALIGNMENT);
		
		JButton  buttonChooseBook = new JButton("Open");
		buttonChooseBook.setAlignmentX(CENTER_ALIGNMENT);
		
		buttonChooseBook.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	//TODO: Open CalendarPane for selected AppointmentBook
            	JTabbedPane jtp = (JTabbedPane) getParent().getParent().getParent();
            	CalendarPane calPane = new CalendarPane(booksList.get(bookSelectorCombo.getSelectedIndex()));
            	jtp.addTab((String) bookSelectorCombo.getSelectedItem(), calPane);
            	
            }
        });
		
		//Damn errors...
		add(pleaseChoose);
		add(bookSelectorCombo);
		add(buttonChooseBook);
	}
	
	public void populateCombo()
	{
		bookSelectorCombo.removeAllItems();
		for(int i = 0; i < booksList.size(); i++)
		{
			//System.out.println(booksList.get(i).appointmentBookName);
			bookSelectorCombo.addItem(booksList.get(i).appointmentBookName);
		}
	}

}
