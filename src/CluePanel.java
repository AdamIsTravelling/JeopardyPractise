import java.awt.event.ActionEvent;

import javax.swing.*;

import java.awt.*;

// Basically a JButton that, when clicked, will show a Full screen JButton 
public class CluePanel extends JeopardyPanel 
{
	
	protected String clueText;
	protected String answerText;
	protected String clue; // Wait I honestly don't know what this does. 
	
	protected boolean hasBeenClickedOnce = false;
	protected boolean isFullScreen = false;
	
	protected boolean isDailyDouble = false;
	protected boolean dailyDoubleWagerScreenShown = false;
	protected boolean clueHasBeenDisplayed = false;
	protected boolean answerHasBeenDisplayed = false;
	
	protected JeopardyScreen parent = null; 
	
	// When the button goes full screen, it is done pretty brutally by just replacing the content pane. 
	// To go back to the previous screen, we replace it with the "Old" content pane, which more or less had the games visual state
	protected Container previousContentPane = null;
	
	// we clone the object in order to set the content pane 
	public CluePanel clone() throws CloneNotSupportedException
	{
		CluePanel cloned = new CluePanel( this.label, this.clue, this.answerText, 
											this.isDailyDouble, this.dailyDoubleWagerScreenShown, 
											this.clueHasBeenDisplayed, this.answerHasBeenDisplayed, this.isFullScreen, this.parent);
			
		return cloned;
	}
	
	// The only constructor I'm going to care about for now
	public CluePanel( String amount, String clue, String answer, boolean isDailyDouble, 
							boolean dailyDoubleWagerScreenShown, boolean clueHasBeenDisplayed,
							boolean answerHasBeenDisplayed , boolean isFullScreen,
							JeopardyScreen parent) 
	{
		super( amount );
		
		this.clue = clue;
		this.clueText = clue;
		this.answerText = answer;
		
		this.isDailyDouble = isDailyDouble;
		this.dailyDoubleWagerScreenShown = dailyDoubleWagerScreenShown;
		this.clueHasBeenDisplayed = clueHasBeenDisplayed;
		this.answerHasBeenDisplayed = answerHasBeenDisplayed;
		this.isFullScreen = isFullScreen;
		this.parent = parent;
		
		// This does almost all of the heavy lifting
		this.addActionListener( new CluePaneAction( "Whatever"));
	}
	
	public CluePanel( Clue clue, JeopardyScreen parent )
	{
		super(  clue.isNull() ? "" : String.valueOf(clue.value) );
	
		this.parent = parent;
		if( clue.getClass().getName() == "NullClue")
		{
			Logger.log( "But it is a null Clue!" );
			this.clue = this.clueText = this.answerText = "";
			this.setHasBeenClickedOn();
		}
		else
		{
			this.clue = clue.text;
			this.clueText = clue.text;
			
			this.answerText = clue.answer;
			this.isDailyDouble = clue.isDailyDouble;
			
			Logger.log( "new clue " + clue.value + " w/Parent " + this.parent);
			this.addActionListener( new CluePaneAction( this.answerText ));
		}
		
	}
	
	public void setHasBeenClickedOn( )
	{
		this.hasBeenClickedOnce = true;
	}
	
	public void setIsFullScreen()
	{
		this.isFullScreen = true;
	}
	
	public void setPreviousContentPane( Container previousPane )
	{
		this.previousContentPane = previousPane;
	}
	
	public void clueRevealed()
	{
		// once the clue has been answered, indicate to the Jeopardy Screen that this clue has been, well, answered.
		this.parent.clueRevealed();
	}
	
	// OK. This private class handles the clicks
	@SuppressWarnings("serial")
	private class CluePaneAction extends AbstractAction 
	{
		private Container previousContentPane = null;
		// first click should produce the clue, full screen
		
		public CluePaneAction(String name) 
		{
			super(name);
		       
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if( CluePanel.this.isDailyDouble )
			{
				if( ! CluePanel.this.dailyDoubleWagerScreenShown )
				{
					Logger.log( "Showing Daily Double wager screen");
					CluePanel.this.label = "DAILY DOUBLE! Pew-pew-pew";
					CluePanel.this.dailyDoubleWagerScreenShown = true;
					this.goFullScreen();
					return;
				}
				
			}
			
			if( !CluePanel.this.clueHasBeenDisplayed )
			{ 
				CluePanel.this.clueHasBeenDisplayed = true;

				CluePanel.this.label = CluePanel.this.clueText;
			
				// It may already be fullscreen, of course
				if( ! CluePanel.this.isFullScreen)
					this.goFullScreen();
				
				
			}
			else if( !CluePanel.this.answerHasBeenDisplayed )
			{
				// otherwise, we probably want to see the answer
				CluePanel.this.answerHasBeenDisplayed = true;
				CluePanel.this.label = CluePanel.this.answerText;
			}
			else 
			{
				this.backToJeopardyBoard();
				CluePanel.this.clueRevealed();
			}
		} 
		  

		protected void goFullScreen()
		{
			Logger.log( "goFullScreen()" );
			Window w = SwingUtilities.windowForComponent(CluePanel.this);
			   
			// Okay, so we should make this the size of the current window
			
	        if (w instanceof JFrame) 
	        {
	        	// the window
	            JFrame frame = (JFrame) w;
	            //frame.dispose();
	            //frame.setUndecorated(true);
	              
	            // OK don't worry
	            //frame.getGraphicsConfiguration().getDevice().setFullScreenWindow(w);
	            this.previousContentPane = frame.getContentPane();
	            
	            try {

	            CluePanel tmp = CluePanel.this.clone();
	            tmp.setHasBeenClickedOn();
	            tmp.setIsFullScreen();
	            tmp.setPreviousContentPane( this.previousContentPane );

	            frame.setContentPane(tmp); // let's try and avoid this...
	            
	            // for the original label, we want it to be not full screen afterwards
	            CluePanel.this.label = "";
				CluePanel.this.setEnabled( false) ;

	            tmp.setOpaque( true );
	            //frame.getContentPane().add( tmp );
	            
	            //Logger.log( "ContentPane is: " + frame.getContentPane() );
	            
	            frame.revalidate();
	            frame.repaint();
	            frame.setVisible(true);
	            
	            CluePanel.this.isFullScreen = true;
	            
	            }
	            catch( CloneNotSupportedException e )
	            {
	            	  
	            	Logger.log( "Whatever" + e.toString());
	            }
	        }
			  
		}
		  
		protected void backToJeopardyBoard()
		{
			  
			Logger.log("backToJeopardyBoard()");
			Window w = SwingUtilities.windowForComponent(CluePanel.this);
			if (w instanceof JFrame) 
			{
				JFrame frame = (JFrame) w;
	            //frame.dispose();
	            //frame.setUndecorated(false);
	               
	            // frame.getGraphicsConfiguration().getDevice().setFullScreenWindow(null);
	            //frame.setContentPane(previousContentPane);
				
				Logger.log( "Lets remove " + CluePanel.this.hashCode() );
	            //frame.remove( CluePanel.this );
				
	            frame.setContentPane(CluePanel.this.previousContentPane);
	            
	            frame.revalidate();
	            frame.repaint();
	            frame.setVisible(true);
	            CluePanel.this.isFullScreen = false;
	          
	                
	          }
			  
		}
		  
	
	}
	
}
