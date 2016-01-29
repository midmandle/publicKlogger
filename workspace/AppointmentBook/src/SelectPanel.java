import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SelectPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JComboBox<String> bookSelectorCombo = new JComboBox<String>();
	
	public SelectPanel()
	{
		super();
		
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel pleaseChoose = new JLabel("Please choose your appointment book:");
		pleaseChoose.setAlignmentX(CENTER_ALIGNMENT);
		
		//fetch existing AppointmentBooks and load into ComboBox.
		
		

		add(pleaseChoose);
		add(bookSelectorCombo);
	}
	
	public void populateCombo(ArrayList<AppointmentBook> booksList)
	{
		for(int i = 0; i < booksList.size(); i++)
			bookSelectorCombo.addItem(booksList.get(i).appointmentBookName);
	}

}
