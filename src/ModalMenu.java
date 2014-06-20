import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Tools.ModalMenuChoice;

// This is a janky Menu Class. It started off and will probably remain a simple modal option dialog
// because all I needed was to be able to quit the game or go back to the main screen
public class ModalMenu
{
	protected Object[] options = null;
	protected HashMap optmap = null;
	
	public ModalMenu()
	{
		this.options = new Object[] {"Return to Main Menu",
	        			"Quit Game"};
		this.optmap = new HashMap();
		this.optmap.put( 0, ModalMenuChoice.MAIN);
		this.optmap.put( 1, ModalMenuChoice.EXIT);
		this.optmap.put( -1, ModalMenuChoice.NONE);
	}
	
	public ModalMenuChoice showModalMenu( JPanel panel )
	{
			
		int n = JOptionPane.showOptionDialog( panel ,
				"This is not the best menu",
				"*shrug*",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,     //do not use a custom Icon
				this.options,  //the titles of buttons
				options); //default button title
	
		Logger.log( String.valueOf(n) + " : " + ModalMenuChoice.MAIN );
		
		return (ModalMenuChoice) this.optmap.get( n );
		//return null;
	}
	
	
}
