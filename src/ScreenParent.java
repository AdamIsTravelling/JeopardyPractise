import javax.swing.*; 

import java.awt.*; 
import java.awt.event.*; 

// This is the main Jeopardy Screen class
public class ScreenParent {

	public static final Color JEOPARDY_BLUE = new Color( 6, 12, 233 );
	
	protected JPanel jpanel;
	
	protected Color backgroundColor;
	protected Controller controller;
	
	ScreenParent()
	{
		// do nothing...
		Logger.log( "New ScreenParent() - No Controller! " );
	}
	
	ScreenParent( Controller controller )
	{
		this.controller = controller;				

		// 6, 12, 133 is the Jeopardy colours!
		this.backgroundColor = JEOPARDY_BLUE;
		// okay lets setup a content panel
	
		this.jpanel = new JPanel();
		this.jpanel.setBackground( this.backgroundColor );
	}
	
	public JPanel getScreen()
	{
		return this.jpanel;
	}
}
