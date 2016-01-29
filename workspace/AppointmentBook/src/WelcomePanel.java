import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class WelcomePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton buttonSelect;
	private JButton buttonCreateNew;
	private JLabel welcomeInstructionsLabel;
	private JPanel buttonPanel;
	
	public WelcomePanel(final ArrayList<AppointmentBook> booksList)
	{
		super();
		setName("Welcome Panel");
		
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Welcome"));
		
		welcomeInstructionsLabel = new JLabel("Please select an existing appointment book or create a new book.");
		welcomeInstructionsLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		buttonSelect = new JButton("Select");
		buttonCreateNew = new JButton("Create");
		
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

		
		
		
		final JPanel cardsPanel = new JPanel();
		
		cardsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		final SelectPanel selectPanel = new SelectPanel();
		selectPanel.setLayout(new BoxLayout(selectPanel, BoxLayout.PAGE_AXIS));
		selectPanel.populateCombo(booksList);
		
		final CreatePanel createPanel = new CreatePanel(booksList);
		createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.PAGE_AXIS));
		
		cardsPanel.setLayout(new CardLayout());
		cardsPanel.add(selectPanel, "Select Panel");
		cardsPanel.add(createPanel, "Create Panel");
		
		buttonPanel.add(buttonSelect);
		buttonPanel.add(buttonCreateNew);
		
		buttonSelect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	CardLayout cl = (CardLayout)cardsPanel.getLayout();
            	cl.show(cardsPanel, "Select Panel");
            }
        });
		
		buttonCreateNew.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	CardLayout cl = (CardLayout)cardsPanel.getLayout();
            	cl.show(cardsPanel, "Create Panel");
            }
        });
		
		
		
		add(welcomeInstructionsLabel);
		add(Box.createVerticalGlue());
		add(cardsPanel);
		add(buttonPanel);
	}
}
