import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Tools.FinalJeopardyStage;


public class FinalJeopardyScreen extends ScreenParent
{
	protected Category category;
	protected Clue clue;
	
	protected JeopardyPanel mainScreen = null;;
	
	protected FinalJeopardyStage stage;
	
	public FinalJeopardyScreen( Controller controller, Round round ) {
		super(controller);
		
		this.category = round.categories.get(0);
		this.clue = this.category.finalJeopardyClue;
		
		this.stage = FinalJeopardyStage.TITLE;
		this.setupScreen();
	}
	
	protected void setupScreen()
	{
		this.mainScreen = new JeopardyPanel( "FINAL JEOPARDY");
		this.mainScreen.setBorderPainted( false );
		
		this.mainScreen.addActionListener( new FinalJeopardyAction( "FINAL JEOPARDY" ));
		
		this.jpanel.setLayout( new BorderLayout(0,0));
		this.jpanel.add( this.mainScreen );
	}
	
	
	// OK. This private class handles the clicks
	@SuppressWarnings("serial")
	private class FinalJeopardyAction extends AbstractAction 
	{
		private Container previousContentPane = null;
		// first click should produce the clue, full screen
		
		public FinalJeopardyAction(String name) 
		{
			super(name);
		       
		}
			
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// A click has happened!
			switch( FinalJeopardyScreen.this.stage )
			{
				case TITLE:
					FinalJeopardyScreen.this.stage = FinalJeopardyStage.CATEGORY;
					FinalJeopardyScreen.this.mainScreen.label = FinalJeopardyScreen.this.category.name;
					break;
				case CATEGORY:
					FinalJeopardyScreen.this.stage = FinalJeopardyStage.CLUE;
					FinalJeopardyScreen.this.mainScreen.label = FinalJeopardyScreen.this.clue.text;
					break;
				case CLUE:
					FinalJeopardyScreen.this.stage = FinalJeopardyStage.ANSWER;
					FinalJeopardyScreen.this.mainScreen.label = FinalJeopardyScreen.this.clue.answer;
					break;
				case ANSWER:
					Logger.log( "Final Jeopardy is complete!");
					FinalJeopardyScreen.this.controller.startNextRound();
					break;
			}
			
		}
	}
}
