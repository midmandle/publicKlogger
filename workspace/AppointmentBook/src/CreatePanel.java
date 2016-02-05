import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * CreatePanel is a panel which facilitates the creation of new AppointmentBook objects and adding them to the GUI global booksList.
 * @author 14061121
 *
 */
public class CreatePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	JTextField textFieldBookName = new JTextField();
	JButton buttonAddNewBook = new JButton("Add");

	/**
	 * Constructor which adds various labels and formatting to the JPanel object. Also implements a JTextfield and an JButton which accesses the textfield and creates
	 * a new AppointmentBook with the String contents. 
	 * @param booksList
	 */
	public CreatePanel(final ArrayList<AppointmentBook> booksList)
	{
		super();
		
		textFieldBookName.setMaximumSize(new Dimension(150, 20));
		
		JLabel pleaseCreate = new JLabel("Please create your appointment book:");
		pleaseCreate.setAlignmentX(CENTER_ALIGNMENT);
		
		
		buttonAddNewBook.setAlignmentX(CENTER_ALIGNMENT);
		buttonAddNewBook.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	booksList.add(new AppointmentBook(textFieldBookName.getText()));
            }
        });
		
		add(pleaseCreate);
		add(textFieldBookName);
		add(buttonAddNewBook);
	}
}
