import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

import javax.swing.*;


public class JeopardyPanel extends JButton {

	protected final static int MAX_FONT_SIZE = 100; // what if we dynamically generated these!?!?!?!?!?
	protected final static int MIN_FONT_SIZE = 14;
	protected final static Color backgroundColor = JeopardyScreen.JEOPARDY_BLUE;
	protected Color textColor = Color.WHITE;
	protected int currentFontSize = 14;
	protected String label = "";
	
	public JeopardyPanel() 
	{
		// TODO Auto-generated constructor stub
		this.setBackground( this.backgroundColor );
	}

	public JeopardyPanel(Icon arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public JeopardyPanel(Action arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public JeopardyPanel(String arg0, Icon arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public JeopardyPanel(String content) {
		super( content );
		
		this.label = content;
		this.currentFontSize = this.getIdealFontSize(null, true);
		this.setFont( new Font( Font.DIALOG, Font.BOLD, this.currentFontSize) );
		this.setBackground( JeopardyScreen.JEOPARDY_BLUE );

		this.setForeground( this.textColor );
		
	}

	// a custom paint method that basically looks for the ideally sized font
	// and then changes as necessary
	public void paint( Graphics g) 
	{
		this.setFocusPainted(false); // Focus looks uuuuugly
		
		this.setFontSize( g );
		//if( ! this.getText().equals(this.label))
		{
			int fsize = this.getFontSize( g );
			//Logger.log("Got font size " + fsize );
			String content = "<html>" +
		            "<body style='text-align:center;background-color:"+ 
					JeopardyPanel.backgroundColor +"; width:auto;font-size:"+ fsize+ ";'>" +
		            "<p>" +
		            this.label +
		            "</p></html>";
		    
			this.setText( content );
		}
		this.setFontSize( g );
		super.paint( g );
	}
	
	public void setFontSize( Graphics g)
	{
		int fontSizeToUse = this.getFontSize( g );
		Font labelFont = this.getFont();
	
		this.setFont(new Font(labelFont.getName(), labelFont.getStyle(), fontSizeToUse));
	}
	
	// Okay, for getting font sizes we REALLY need some kind of 
	// function where we pass the width of the container, alongside the number of characters
	// and then dal with it from there. But that can be later, for heaven's sake
	
	public int getFontSize( Graphics g )
	{
		int fontSizeToUse = this.getIdealFontSize( g, false );
		// Logger.log("Trying to Return font size " + fontSizeToUse);
		
		if( fontSizeToUse <= this.currentFontSize + 2 && 
				fontSizeToUse >= this.currentFontSize - 2)
		{
			// then don't bother.
			fontSizeToUse = this.currentFontSize;
		}
		else
			this.currentFontSize = fontSizeToUse;
		
		if( fontSizeToUse > MAX_FONT_SIZE)
			fontSizeToUse = MAX_FONT_SIZE;
		
		if( fontSizeToUse < MIN_FONT_SIZE)
			fontSizeToUse = MIN_FONT_SIZE;
		return fontSizeToUse;
		
	}
	
	// We want to return the font size that will 
	public int getIdealFontSize( Graphics g, boolean initial )
	{
		int idealFontSize = 14;

		if( initial)
				return idealFontSize;

		//int stringWidth = this.getFontMetrics(labelFont).stringWidth(labelText) *2 + 1;
		int componentWidth = this.getWidth();

		// Ideal font size is like, 18 characters per line
		int newFontSize = componentWidth / 18;
		int componentHeight = (this.getHeight() / 5);
		
		//Logger.log( "newFontSize " + newFontSize + " component Height " + componentHeight);
		// Pick a new font size so it will not be larger than the height of label.
		idealFontSize = Math.min(newFontSize, componentHeight);
		
		return idealFontSize;
	}
	
}
