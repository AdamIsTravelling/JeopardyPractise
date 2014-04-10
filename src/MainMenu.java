import javax.swing.*; 

import java.awt.*; 
import java.awt.event.*; 

public class MainMenu extends ScreenParent {

	protected JButton startButton;
	
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
		
		this.jpanel.setLayout( new FlowLayout() );
		
		this.jpanel.add( this.startButton );
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
			  MainMenu.this.controller.attemptToStartGame();
		  }
		  
		
	}
	
}
