import javax.swing.*; 
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import java.awt.*; 
import java.awt.event.*; 
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainMenu extends ScreenParent {

	
	protected int gameID; // this will be the "Jeopardy Archive ID" i.e. jarchiveID in the game table
	protected int lastGamePlayedID; // see above
	
	protected JPanel inputPanel;
	protected JPanel messagePanel;
	
	protected JTextField gameNumberEntry;
	protected JButton startButton;
	
	// a simple field to display any errors that may occur
	protected JLabel errorField;
	protected String errorText;
	
	public MainMenu()
	{
		Logger.log( "New MainMenu() " +  this);
	}
	
	public MainMenu( Controller controller ) 
	{
		super( controller );
		Logger.log( "New MainMenu(" + this.controller + ") " +  this);
		
		// Create a start button
		this.startButton = new JButton( "Click Here To Get Started" );
		this.startButton.addActionListener(  new StartButtonAction( "Action" ) );
		
		//NumberFormat nf = NumberFormat.getIntegerInstance();
		
		//this.gameNumberEntry = new JFormattedTextField(nf );
		//this.gameNumberEntry.setPreferredSize( new Dimension( 55, 25 ) );
		//this.gameNumberEntry.setValue( 2 );
		
		this.gameNumberEntry = new JTextField( "2", 5);
		
		this.initInputPanel();
		this.initMessagePanel();
		
		// we want a box layout!
		BoxLayout layout = new BoxLayout(this.jpanel, BoxLayout.Y_AXIS);
		this.jpanel.setLayout( layout );
		
		// These are the parts that will let manually enter the game number

		this.jpanel.add( this.inputPanel );
		this.jpanel.add( this.messagePanel );
	}
	
	protected void initInputPanel()
	{
		this.inputPanel = new JPanel( new FlowLayout() );
		this.inputPanel.setBackground( this.backgroundColor );
		
		this.inputPanel.add( this.gameNumberEntry);
		this.inputPanel.add( this.startButton);
	
	}
	
	protected void initMessagePanel()
	{
		this.messagePanel = new JPanel( new FlowLayout() );
		this.messagePanel.setBackground( this.backgroundColor );
		
		this.errorField = new JLabel( "" );
		this.errorField.setForeground( Color.RED );
		this.messagePanel.add( this.errorField);
	}
	
	public void displayErrorText( String text )
	{
		this.errorText = text;
		this.errorField.setText( text );
	}
	
	public void clearErrorText()
	{
		this.errorText = "";
		this.errorField.setText("");
		
	}
	
	// jacked this from a website - long and short it is a nested class to help
	// with actions when things are clicked. Toot toot!
	@SuppressWarnings("serial")
	private class StartButtonAction extends AbstractAction 
	{
		public StartButtonAction(String name) {
		         super(name);
		         
		      }
		
		  @Override
		  public void actionPerformed(ActionEvent e)
		  {
			  // So you can do this, eh? Swag
			  // the controller will  check for validity, etc.
			  String boxText = MainMenu.this.gameNumberEntry.getText();
			  try {
				  Integer tmp  = Integer.valueOf( boxText );
				  MainMenu.this.controller.attemptToStartGame( tmp );
			  } catch (NumberFormatException f)
			  {
				  // Can't format an integer, so I just ignore it for now. 
			  }
			 
			 
		  }
		  
		
	}
	
}
