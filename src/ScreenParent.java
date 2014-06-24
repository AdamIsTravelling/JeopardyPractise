import javax.swing.*;

import java.awt.*; 
import java.awt.event.*; 

import Tools.ModalMenuChoice;

// This is the main Jeopardy Screen class
public class ScreenParent {

	public static final Color JEOPARDY_BLUE = new Color( 6, 12, 233 );
	
	protected JPanel jpanel;
	
	protected Color backgroundColor;
	protected Controller controller;
	
	// Add a modal control to all Jeopardy Screen games?
	protected boolean showMenu = true;
	protected ModalMenu modalMenu = null;
	
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

		this.setupKeyBindings();

		this.modalMenu = controller.modalMenu;
	}
	
	public JPanel getScreen()
	{
		return this.jpanel;
	}
	
	protected void disableMenu()
	{
		this.showMenu = false;
	}
	
	protected void setupKeyBindings()
	{
		Logger.log( "setting up key bindings");
		this.jpanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE , 0), "AdamTest");
		this.jpanel.getActionMap().put( "AdamTest" , new EscapeButtonAction( "Ugh" ) );
		Logger.log( "We have set us up the key bindings");
	}

	@SuppressWarnings("serial")
	private class EscapeButtonAction extends AbstractAction 
	{	
		public EscapeButtonAction(String name) 
		{
			super(name);
		       
		}
		
		public void actionPerformed(ActionEvent e)
		{			
			if( ScreenParent.this.showMenu && ScreenParent.this.modalMenu != null)
			{
				switch( ScreenParent.this.modalMenu.showModalMenu( ScreenParent.this.jpanel ) )
				{
					case NONE:
						Logger.log("They didn't choose shit");
						break;
					case MAIN: 
						Logger.log( "They want to go to the Main Menu");
						ScreenParent.this.controller.endGame();
						break;
					case MAINANDMARK:
						Logger.log( "They want to return to the Main Menu AND mark it as having been used");
						ScreenParent.this.controller.endGame();
						break;
					case EXIT:
						Logger.log( "They want to get the fuck out of here" );
						System.exit(0);
						break;
					default: 
						Logger.log( "Someone clicked something" );
				}
			}
		}
	}
	
}
