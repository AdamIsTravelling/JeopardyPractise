import javax.swing.*; 
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import java.awt.*; 
import java.awt.event.*; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainMenu extends ScreenParent {

	
	protected int gameID; // this will be the "Jeopardy Archive ID" i.e. jarchiveID in the game table
	protected Integer lastGamePlayedID = 0; // Likewise
	protected Integer nextGameToBePlayedID = 0;
	
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
		
		this.getLastGamePlayedID();
		this.updateNextGameToPlayID();
		
		//NumberFormat nf = NumberFormat.getIntegerInstance();
		
		//this.gameNumberEntry = new JFormattedTextField(nf );
		//this.gameNumberEntry.setPreferredSize( new Dimension( 55, 25 ) );
		//this.gameNumberEntry.setValue( 2 );
		
		this.gameNumberEntry = new JTextField( this.nextGameToBePlayedID.toString(), 5);
		
		this.initInputPanel();
		this.initMessagePanel();
		
		// we want a box layout!
		BoxLayout layout = new BoxLayout(this.jpanel, BoxLayout.Y_AXIS);
		this.jpanel.setLayout( layout );
		
		// These are the parts that will let manually enter the game number

		this.jpanel.add( this.inputPanel );
		this.jpanel.add( this.messagePanel );
	}
	
	protected void getLastGamePlayedID()
	{
		String query = "SELECT ConstantValue FROM jeopardy.values WHERE ConstantName LIKE 'LastGameID'"; 
		ResultSet results = Database.runQuery( query );

		try
		{
			while( results.next() )
			{
				this.lastGamePlayedID = results.getInt("ConstantValue");
			}
		}
		catch (SQLException e)
		{
			Logger.log("MainMenu::getLastGamePlayedID() SQL Error " + e.toString() );
			this.lastGamePlayedID = new Integer(0);
		}
	}
				
	
	
	protected void updateNextGameToPlayID()
	{
		String query = "SELECT jarchiveID FROM game WHERE jarchiveID > " + this.lastGamePlayedID.toString() + " ORDER BY jarchiveID ASC LIMIT 0,1"; 
		ResultSet results = Database.runQuery( query );

		try
		{
			while( results.next() )
			{
				this.nextGameToBePlayedID = results.getInt("jarchiveID");
			}
		}
		catch (SQLException e)
		{
			Logger.log("MainMenu::updateNextGameToPlayID() SQL Error " + e.toString() );
			this.nextGameToBePlayedID = new Integer(1); // start at the beginning
		}
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
