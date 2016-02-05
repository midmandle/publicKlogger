import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class for setting up the JFrame in which the rest of the JPanels will be placed.
 * @author 14061121
 *
 */
public class MainFrame extends JFrame {

	
	private static final long serialVersionUID = 1L;
	ArrayList<AppointmentBook> booksList = new ArrayList<AppointmentBook>();
	JTabbedPane jtp = new JTabbedPane();
	
	/**
	 * Standard constructor to set up the JFrame. Also initialises the list of AppointemntBooks from the DB with the use of HelperMethods static class.
	 */
	public MainFrame()
	{
		super("Appointment Book");
		HelperMethods.InitialiseBooksFromDatabase(booksList);
		initialiseUI();
		//HelperMethods.SaveBooksToCSV("csv.csv", booksList.get(0));
		//HelperMethods.FetchBooksFromCSV("csv.csv", booksList.get(0));
		
	}
	
	/**
	 * Initialises the basic aspects of the JFrame. Sets size, creates and populates MenuBar, sets action on close and sets to visible. Boilerplate code.
	 */
	private void initialiseUI()
	{	
		

		WelcomePanel welcomePanel = new WelcomePanel(booksList);
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.PAGE_AXIS));
		jtp.addTab("Welcome", welcomePanel);
		setContentPane(jtp);
		
		createMenuBar();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000, 600);
		this.setVisible(true);
	}
	
	/**
	 * Creates and populates the MenuBar. Also creates the relevant ActionListeners for each MenuItem.
	 */
	private void createMenuBar()
	{
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem saveToCSV = new JMenuItem("Save as .CSV");
		JMenuItem loadFromCSV = new JMenuItem("Load from .CSV");
		JMenuItem forceSaveToDB = new JMenuItem("FORCE Save to DB");
		JMenuItem exportToICS = new JMenuItem("Export to ICS");
		JMenuItem importFromICS = new JMenuItem("Import from ICS");
		
		WelcomePanel welcomeP = (WelcomePanel) jtp.getComponent(0);
    	JPanel cardsP = (JPanel) welcomeP.getComponent(2);
    	final SelectPanel selectP = (SelectPanel) cardsP.getComponent(0);
		
		saveToCSV.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fileChooser = new JFileChooser();
            	FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("CSV FILES", "csv");
            	fileChooser.setFileFilter(fileFilter);
            	int retVal = fileChooser.showSaveDialog(selectP);
            	
            	 if (retVal == JFileChooser.APPROVE_OPTION) {
                     File file = fileChooser.getSelectedFile();
                     int confirm = JOptionPane.showConfirmDialog(selectP, "Saving "+ selectP.bookSelectorCombo.getSelectedItem()+" to "+file.getAbsolutePath());
                     if(confirm == JOptionPane.OK_OPTION)
                    	 HelperMethods.SaveBooksToCSV(file.getAbsolutePath(), booksList.get(selectP.bookSelectorCombo.getSelectedIndex()));
                     else
                    	 return;
            	 }
            }
        });
	
		loadFromCSV.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fileChooser = new JFileChooser();
            	FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("CSV FILES", "csv");
            	fileChooser.setFileFilter(fileFilter);
            	
            	int retVal = fileChooser.showOpenDialog(selectP);
            	
	           	if (retVal == JFileChooser.APPROVE_OPTION) {
	                   File file = fileChooser.getSelectedFile();
	                   int confirm = JOptionPane.showConfirmDialog(selectP, "Loading "+ selectP.bookSelectorCombo.getSelectedItem()+" from "+file.getAbsolutePath());
	                   if(confirm == JOptionPane.OK_OPTION)
	                   {
	                	   HelperMethods.FetchBooksFromCSV(file.getAbsolutePath(), booksList);
	                	   selectP.populateCombo();
	                   }
	                   else
	                	   return;
	           	}
            }
        });
		
		forceSaveToDB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	HelperMethods.ForceSaveToDB(booksList.get(selectP.bookSelectorCombo.getSelectedIndex()));
            }
        });
		
		exportToICS.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fileChooser = new JFileChooser();
            	FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("ICS FILES", "ics");
            	fileChooser.setFileFilter(fileFilter);
       
            	int retVal = fileChooser.showSaveDialog(selectP);
            	
            	 if (retVal == JFileChooser.APPROVE_OPTION) {
                     File file = fileChooser.getSelectedFile();
                     int confirm = JOptionPane.showConfirmDialog(selectP, "Exporting "+ selectP.bookSelectorCombo.getSelectedItem()+" to "+file.getAbsolutePath());
                     if(confirm == JOptionPane.OK_OPTION)
                    	 HelperMethods.ExportAsICS(booksList.get(selectP.bookSelectorCombo.getSelectedIndex()), file.getAbsolutePath());
                     else
                    	 return;
            	 }
            }
        });
		
		importFromICS.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fileChooser = new JFileChooser();
            	FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("ICS FILES", "ics");
            	fileChooser.setFileFilter(fileFilter);
       
            	int retVal = fileChooser.showOpenDialog(selectP);
            	
            	if (retVal == JFileChooser.APPROVE_OPTION) {
	                   File file = fileChooser.getSelectedFile();
	                   int confirm = JOptionPane.showConfirmDialog(selectP, "Importing "+ file +" from "+file.getAbsolutePath());
	                   if(confirm == JOptionPane.OK_OPTION)
	                   {
	                	   HelperMethods.ImportAsICS(booksList, file.getAbsolutePath());
	                	   selectP.populateCombo();
	                   }
	                   else
	                	   return;
	           	}
            }
        });
		
		file.add(saveToCSV);
		file.add(loadFromCSV);
		file.add(forceSaveToDB);
		file.add(importFromICS);
		file.add(exportToICS);
		menu.add(file);
		setJMenuBar(menu);
	}
}
