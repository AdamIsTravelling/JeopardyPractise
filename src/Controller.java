import javax.swing.*; 

import java.awt.*; 
import java.awt.event.*; 


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
	
	Controller(String title) 
    { 
		Logger.log( "New Controller(" + title + ") " + this );
		this.jepTitle = title;
		Database.initDB();
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
		//this.doubleJeopardy = new JeopardyScreen( this, game.getDoubleJeopardy() );
		Logger.log( "Done");
	}
	
	public void init()
	{
		this.initJFrame();

		// Init menu - don't bother initializing the single and double jeopardy games until a game is started
		this.initMenu();
	}
	
	// START The Program
    public void gunIt(  )
    {
    	// Some main menu bs
    	this.window.add( this.menu.getScreen() );
    	
    	// TODO ignore this guy down the road
	
    	//this.contentPane.add( this.singleJeopardy.getScreen(), new Integer( 100 ));
    	// this.attemptToStartGame();
    	//this.window.add( this.singleJeopardy.getScreen());
    	//this.window.add( this.contentPane, BorderLayout.CENTER );
    	
    	this.window.setVisible(true);
    }
	
	// HANDLE INTERACTIONS FROM THE VIEWS
	public void attemptToStartGame()
	{
		// First, build the game object
		Game gameToPlay = new Game( 4612 );
		
		// Then, ask if it is a valid game
	
		this.initSingleJeopardyScreen( gameToPlay );
		this.initDoubleJeopardyScreen( gameToPlay );
		
		// Dismiss the main menu
		this.dismissMainMenu();
		
		// then, start the fucker!
		this.startGame( gameToPlay );
	}
	
	protected void startGame( Game game )
	{
		Logger.log( "Starting game #" + game.jArchiveID );
    	
    	this.window.remove( this.menu.getScreen() );
    	
    	
		//System.exit( 0 ); // TODO get rid of this guy
	}
	
    // TELL THE VIEWS WHAT TO DO
    protected void dismissMainMenu()
    {
    	if( this.menu == null )
    		return;
    	
    	Logger.log( "Controller dismissMainMenu()");
    	
    	this.window.remove( this.menu.getScreen() );
    	
    	this.window.add( this.singleJeopardy.getScreen() );
    	this.window.revalidate();
    	this.window.repaint();
    }
    
    
}