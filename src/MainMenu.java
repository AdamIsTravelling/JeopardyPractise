import javax.swing.*; 
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import java.awt.*; 
import java.awt.event.*; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MainMenu extends ScreenParent {

	
	protected int gameID; // this will be the "Jeopardy Archive ID" i.e. jarchiveID in the game table
	protected Integer lastGamePlayedID = 0; // Likewise
	protected Integer nextGameToBePlayedID = 0;
	
	protected JPanel inputPanel;
	protected JPanel messagePanel;
	protected JPanel infoPanel; 
	protected JPanel gameListPanel;
	
	protected JTextField gameNumberEntry;
	protected JButton startButton;
	
	// a simple field to display any errors that may occur
	protected JLabel errorField;
	protected String errorText;

	ArrayList<GameInfo> gameList;
	
	public MainMenu()
	{
		Logger.log( "New MainMenu() " +  this);
	}
	
	public MainMenu( Controller controller ) 
	{
		super( controller );
		
		Logger.log( "New MainMenu(" + this.controller + ") " +  this);
		
		this.disableMenu();
		
		// Create a start button
		this.startButton = new JButton( "Click Here To Get Started" );
		this.startButton.addActionListener(  new StartButtonAction( "Action" ) );
		
		this.getLastGamePlayedID();
		this.updateNextGameToPlayID();
		
		this.gameNumberEntry = new JTextField( this.nextGameToBePlayedID.toString(), 5);
		
		this.initInputPanel();
		this.initMessagePanel();
		this.initInfoPanel();
		this.initGameListPanel();
		
		// we want a box layout!
		BoxLayout layout = new BoxLayout(this.jpanel, BoxLayout.Y_AXIS);
		this.jpanel.setLayout( layout );
		
		// These are the parts that will let manually enter the game number
		this.jpanel.add( this.inputPanel );
		this.jpanel.add( this.messagePanel );
		this.jpanel.add(this.infoPanel);
		this.jpanel.add( this.gameListPanel);
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
	
	protected String getLastGamePlayedInfoString()
	{
		String retval = "";
		// Maybe we just haven't tried to get the last game played
		if( this.lastGamePlayedID.intValue() == 0)
			this.getLastGamePlayedID();
		// Haha Okay it is legitimately 0 now
		if ( this.lastGamePlayedID.intValue() == 0)
			return "Congrats, it is your first game! I hope you have fun";;
		
		retval += "The last game you played was game #" + this.lastGamePlayedID;
		
		String query = "SELECT * FROM game WHERE jarchiveID = " + this.lastGamePlayedID.toString() + " LIMIT 0,1"; 
		ResultSet results = Database.runQuery( query );

		try
		{
			while( results.next() )
			{
				retval += " (aired ";
				retval += results.getString("playDate");
				retval += ")";
				
			}
		}
		catch (SQLException e)
		{
			Logger.log("MainMenu::getLastGamePlayedInfoString() SQL Error " + e.toString() );
			
		}
		
		return retval;
	}
	
	protected void initInputPanel()
	{
		this.inputPanel = new JPanel( new FlowLayout() );
		this.inputPanel.setBackground( this.backgroundColor );
		
		this.inputPanel.add( this.gameNumberEntry);
		this.inputPanel.add( this.startButton);

		this.inputPanel.setMaximumSize( this.inputPanel.getPreferredSize() );
		
	}
	
	protected void initMessagePanel()
	{
		this.messagePanel = new JPanel( new FlowLayout() );
		this.messagePanel.setBackground( this.backgroundColor );
		
		this.errorField = new JLabel( "" );
		this.errorField.setForeground( Color.RED );
		
		
		this.messagePanel.add( this.errorField);
		this.messagePanel.setMaximumSize( this.messagePanel.getPreferredSize() );
	}
	
	protected void initInfoPanel()
	{
		this.infoPanel = new JPanel( new FlowLayout() );
		this.infoPanel.setBackground( this.backgroundColor );
		JLabel info = new JLabel( this.getLastGamePlayedInfoString() );

		info.setForeground( Color.WHITE );
		
		this.infoPanel.add(info);
		this.infoPanel.setMaximumSize( this.infoPanel.getPreferredSize() );
	}
	
	protected void initGameListPanel()
	{
		this.gameListPanel = new JPanel( new FlowLayout() );
		
		this.initGameList();
		
		// This is the list containing the first X games to use 
		// Will need to contain at least a string to represent the game, as well as a gameID
		
		DefaultListModel model = new DefaultListModel();
		for( int i = 0; i < this.gameList.size() ; i ++)
		{
			model.addElement( this.gameList.get(i) );
			Logger.log("Successfully added a gameID");
		}
		
		//JList<Object> list = new JList<>( this.gameList.toArray() );

		JList<Object> list = new JList<>( model );
		list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		list.setSelectedIndex( 0 );
		    
		JScrollPane scroller = new JScrollPane( list );
		
		scroller.getViewport().setBackground( this.backgroundColor );
		scroller.setBorder(BorderFactory.createEmptyBorder());
		scroller.revalidate();
		
		this.gameListPanel.add( scroller);
		this.gameListPanel.setMaximumSize( this.gameListPanel.getPreferredSize() );

		this.gameListPanel.revalidate();
	}
	
	// Iniitializes the ArrayList of games
	protected void initGameList()
	{
		
		this.gameList = controller.getGameList();
		
	}
	
	public void displayErrorText( String text )
	{
		this.errorText = text;
		this.errorField.setText( text );
		this.messagePanel.setMaximumSize( this.messagePanel.getPreferredSize() );
	}
	
	public void clearErrorText()
	{
		this.errorText = "";
		this.errorField.setText("");
		
		this.messagePanel.setMaximumSize( this.messagePanel.getPreferredSize() );
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
