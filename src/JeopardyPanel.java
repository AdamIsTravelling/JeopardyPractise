import java.awt.Color;
import java.awt.Font;
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
		this.currentFontSize = this.getIdealFontSize(true);
		this.setFont( new Font( Font.DIALOG, Font.BOLD, this.currentFontSize) );
		this.setBackground( JeopardyScreen.JEOPARDY_BLUE );

		this.setForeground( this.textColor );
		
	}

	// a custom paint method that basically looks for the ideally sized font
	// and then changes as necessary
	public void paint( Graphics g) 
	{
		this.setFocusPainted(false); // Focus looks uuuuugly
		
		
		this.setFontSize();
		//if( ! this.getText().equals(this.label))
		{
			int fsize = this.getFontSize();
			Logger.log("Got font size " + fsize );
			String content = "<html>" +
		            "<body style='text-align:center;background-color:"+ 
					JeopardyPanel.backgroundColor +"; width:auto;font-size:"+ fsize+ ";'>" +
		            "<p>" +
		            this.label +
		            "</p></html>";
		    
			this.setText( content );
		}
		this.setFontSize();
		super.paint( g );
	}
	
	public void setFontSize()
	{
		int fontSizeToUse = this.getFontSize();
		Font labelFont = this.getFont();
		this.setFont(new Font(labelFont.getName(), labelFont.getStyle(), fontSizeToUse));
	}
	
	
	public int getFontSize()
	{
		int fontSizeToUse = this.getIdealFontSize( false );
		Logger.log("Trying to Return font size " + fontSizeToUse);
		
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
	public int getIdealFontSize( boolean initial )
	{
		int idealFontSize = 16;

		if( initial)
			return idealFontSize;
		// Ideal font size is like, 18 characters per line
		
		Font labelFont = this.getFont();
		String labelText = this.getText();

		//int stringWidth = this.getFontMetrics(labelFont).stringWidth(labelText) *2 + 1;
		int componentWidth = this.getWidth();
		int stringWidth = componentWidth - 10;

		Logger.log( "IdealFontSize: Current font size "+ labelFont.getSize() );  
		Logger.log( "IdealFontSize: Component Width "+ componentWidth + " stringWidth " + stringWidth);  
		
		// Find out how much the font can grow in width - who cares? Right?
		double widthRatio = (double)componentWidth / (double)stringWidth;
		
		
		Logger.log( "IdealFontSize: widthRatio " + widthRatio); 
		
		int newFontSize = (int)(labelFont.getSize() * widthRatio);
		newFontSize = (int) widthRatio;
		int componentHeight = this.getHeight();
		newFontSize = componentWidth / 20;
		
		// Pick a new font size so it will not be larger than the height of label.
		idealFontSize = Math.min(newFontSize, componentHeight);
		
		// so it looks like component height can be the limiter
		Logger.log( "IdealFontSize: Smaller of "+ newFontSize + " and " + componentHeight );
		
		return idealFontSize;
	}
	
}
