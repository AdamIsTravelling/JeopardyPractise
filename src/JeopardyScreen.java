import javax.swing.*; 

import java.awt.*; 
import java.awt.event.*; 
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class JeopardyScreen extends ScreenParent {
	
	public static final int HORIZONTAL_GAP = 5;
	public static final int VERTICAL_GAP = 5;
	
	protected LayoutManager layout;
	protected static final List<String> SINGLE_CLUE_VALUES = Arrays.asList( "200", "400", "600", "800", "1000");
	protected static final List<String> DOUBLE_CLUE_VALUES = Arrays.asList( "400", "800", "1200", "1600", "2000");
	protected static final List<String> HALF_CLUE_VALUES = Arrays.asList( "100", "200", "300", "400", "500");
	
	protected List<String> clueValues; // this will hold the current clue Values 
	protected Round round;
	
	public JeopardyScreen() {
		// TODO Auto-generated constructor stub
	}

	public JeopardyScreen( Controller controller, Round round ) {
		super(controller);
		this.round = round;
		Logger.log( "New JeopardyScreen(" + this.controller + ", " + round + ") " + this );
	
		this.backgroundColor =  Color.BLACK; 
		this.jpanel.setBackground( this.backgroundColor );
		
		// There will always be six columns. this way, we fill things up column by column 
		this.jpanel.setLayout( new GridLayout( 0, 6, HORIZONTAL_GAP, VERTICAL_GAP) ) ;
		this.jpanel.repaint();

		this.clueValues = (true) ? SINGLE_CLUE_VALUES : DOUBLE_CLUE_VALUES;
		
		this.setupScreen();
		
	}
	
	public void setupScreen()
	{
		// first, add all the titles
		this.setupTitles();
		
		// then, Row by Row, add the clues
		this.setupClues();
		
		this.jpanel.repaint();
	
		
	}
	
	protected void setupTitles()
	{
		for( int i = 0 ; i < 6 ; i ++ )
		{
			this.jpanel.add( new CategoryTitle( this.round.categories.get(i).name ) );
			Logger.log( "Added Title " + i + " - " + this.round.categories.get(i).name);
		}
	}

	// setup the clues row by row
	protected void setupClues()
	{
		for( int row = 0; row < 5; row++ )
		{
			for( int col = 0 ; col < 6 ; col ++ )
			{
				Clue tmp = this.round.categories.get( col ).clues.get( row ); 
				this.jpanel.add( new CluePanel( tmp, this));
			}
		}
	}
	
	// When a clue has been 
	public void clueRevealed()
	{
		
		this.controller.clueRevealed();
	}
		
}
