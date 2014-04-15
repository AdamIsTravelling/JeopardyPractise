import javax.swing.*; 

import java.awt.*; 
import java.awt.event.*; 
import java.sql.ResultSet;
import java.sql.SQLException;


// This is the main screen, which handles all the things. 
// It should contain the main JFrame
// It should have 3 or 4 subscreens - Menu, single Jeopardy, and Double Jeopardy. Maybe final
public class Controller {

	static final long serialVersionUID = 18; // this got a warning out of the way
	
	private JFrame window;
	private JLayeredPane contentPane;
	protected String jepTitle; // If there is a title to be had. 
	
	protected MainMenu menu = null; 
	protected JeopardyScreen singleJeopardy = null;
	protected JeopardyScreen doubleJeopardy = null;
	
	protected Game currentGame = null;
	protected Round currentRound = null;
	
	// 1 - first off, this is called by Jeopardy Practise
	Controller(String title) 
    { 
		Logger.log( "New Controller(" + title + ") " + this );
		this.jepTitle = title;
		Database.initDB();
    }
	
	// 2 - Then JeopardyPractise calls init, and 
	public void init()
	{
		this.initJFrame();

		// Init menu - don't bother initializing the single and double jeopardy games until a game is started
		this.initMenu();
	}
	
	// INITIALIZATION FUNCTIONS
	protected void initJFrame()
	{
		this.window = new JFrame();
		this.window.setTitle( this.jepTitle );
        this.window.setSize(500,500); 

        // Uncomment this for fullscreen radness
        //this.window.setUndecorated(true);
        //this.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
	}
	
	protected void initMenu()
	{
		Logger.log( "Initiating Menu Screen...");
		this.menu = new MainMenu( this );
		Logger.log( "Done");
	}

	protected void initSingleJeopardyScreen( Game game )
	{
		Logger.log( "Initiating single Jeopardy Screen...");
		this.singleJeopardy = new JeopardyScreen( this, game.getSingleJeopardy() );
		Logger.log( "Done");
	}

	protected void initDoubleJeopardyScreen( Game game)
	{
		Logger.log( "Initiating Double Jeopardy Screen...");
		this.doubleJeopardy = new JeopardyScreen( this, game.getDoubleJeopardy() );
		Logger.log( "Done");
	}
	
	
	// 3 - this is called after initialization. It displays the main menu
    public void gunIt(  )
    {
    	// Some main menu bs
    	this.window.add( this.menu.getScreen() );
    	
    	// TODO ignore this guy down the road
	
    	// Below is stuff that we can do IF we want to rush directly to a 
    	//this.contentPane.add( this.singleJeopardy.getScreen(), new Integer( 100 ));
    	// this.attemptToStartGame();
    	//this.window.add( this.singleJeopardy.getScreen());
    	//this.window.add( this.contentPane, BorderLayout.CENTER );
    	
    	this.window.setVisible(true);
    }
	
	// HANDLE INTERACTIONS FROM THE VIEWS
	public void attemptToStartGame( Integer gameID )
	{
		// check if this is a valid gameID
		if( ! this.checkGameID( gameID ) )
		{
			Logger.log( "Invalid gameID(" + gameID + ")");
			this.menuDisplayErrorText( "Sorry, game "+ gameID + " doesn't exist" );
			return;
		}

		// First, build the game object
		this.currentGame = new Game( gameID.intValue() );
		
		// Then, ask if it is a valid game
	
		//Start Single Jeopardy!
		this.startSingleJeopardy();
		
	}
	
	protected boolean checkGameID( Integer gameID )
	{
		Logger.log( "checkGameID(" + gameID + ")");
		if( gameID < 1)
			return false;
		
		// check for database existence
		if( !this.gameIDExistsInDatabase( gameID ))
		{
			return false;
		}
		
		return true;
	}
	
	protected boolean gameIDExistsInDatabase( Integer gameID )
	{
		boolean retval = false;
		String query = "SELECT COUNT(*) as `count` FROM jeopardy.game WHERE jarchiveID = '" + gameID + "'";
		
		ResultSet results = Database.runQuery( query );
	
		try
		{
			if( results.next() )
			{
				Logger.log( results.getString( "count") );
				if( results.getString( "count").equalsIgnoreCase("1") )
					retval = true;	
			}
		} catch (SQLException e)
		{
			return false;
		}
		return retval;
	}
	
	
	protected void startSingleJeopardy()
	{
		Logger.log( "Starting single Jeopardy for game #" + this.currentGame.jArchiveID );
		// Initiate the jeopardy Screen
		this.initSingleJeopardyScreen( this.currentGame );
		//this.initDoubleJeopardyScreen( gameToPlay );
		
		this.currentRound = this.currentGame.singleJ;
				
		// Dismiss the main menu
		this.dismissMainMenu();
			
		// add the single jeopardy game
		this.showSingleJeopardy();
		//this.window.remove( this.menu.getScreen() );
		//System.exit( 0 ); // TODO get rid of this guy
	}
	

	
	// 
	protected void startDoubleJeopardy()
	{
		Logger.log( "Starting double jeopardy");
		this.initDoubleJeopardyScreen( this.currentGame );
		this.currentRound = this.currentGame.doubleJ;
		
		this.dismissSingleJeopardy();
		this.showDoubleJeopardy();
		
	}
	
	// gets information from the JeopardyScreen
	public void clueRevealed()
	{
		this.currentRound.clueRevealed();
		
		if( this.currentRound.allCluesRevealed())
		{
			Logger.log( "Round complete!"); 
			
			this.startNextRound();
		}
	}
	
	protected void startNextRound()
	{
	
		this.startDoubleJeopardy();
		
	}
	
	// clean up all the shit
	public void endGame()
	{
		
		
	}
	
    // TELL THE VIEWS WHAT TO DO
    protected void dismissMainMenu()
    {
    	this.dismissScreen( this.menu );
    }
    
	protected void dismissSingleJeopardy()
	{
		this.dismissScreen( this.singleJeopardy );
	}
    
	// take the Double Jeopardy screen off the main panel
	protected void dismissDoubleJeopardy()
	{
		this.dismissScreen( this.doubleJeopardy );
	}
	
	protected void dismissScreen( ScreenParent obj )
	{
	 	Logger.log( "Controller dismiss Screen(" +obj.getClass() + ")" );
		if( obj == null )
    		return;
    	
    	
    	this.window.remove( obj.getScreen() );
    	
    	this.window.revalidate();
    	this.window.repaint();
	}
	
	protected void showMenu()
	{
		this.showScreen(this.menu);
	}
	
	protected void showSingleJeopardy()
	{
		this.showScreen(this.singleJeopardy);
	}
    
	protected void showDoubleJeopardy()
	{
		this.showScreen( this.doubleJeopardy);
	}
	
	protected void showScreen( ScreenParent obj )
	{
		Logger.log( "Controller show Screen(" +obj.getClass() + ")" );
	
		if( obj == null )
			return;
		
		this.window.add( obj.getScreen() );
    	this.window.revalidate();
    	this.window.repaint();
	}
	
	protected void menuDisplayErrorText( String errorText )
    {
    	this.menu.displayErrorText( errorText );
    
    }
    
}