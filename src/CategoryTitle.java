import javax.swing.Action;
import javax.swing.Icon;
import java.awt.Color;


public class CategoryTitle extends JeopardyPanel {

	private static final Color JEOPARDY_GOLD = new Color( 255,215,0);
	protected String title;
	
	protected String content;
	
	public CategoryTitle() {
		// TODO Auto-generated constructor stub
	}

	public CategoryTitle(Icon arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public CategoryTitle(Action arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CategoryTitle(String arg0, Icon arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	// The only constructor we'll be using
	public CategoryTitle(String title) 
	{
		super( title );
		this.textColor = Color.YELLOW;
		this.setForeground( this.textColor );
		//this.setEnabled( false );
		
		this.revalidate();
		this.repaint();
	}

}
