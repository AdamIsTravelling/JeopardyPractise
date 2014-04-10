import org.apache.commons.lang3.StringEscapeUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Clue
{
	public int id = 0; // the database id
	
	public String text = "";
	public String answer = "";
	public int value = 0; // this is the value presented on the screen
	public int wageredValue = 0; //what clue was ultimately worth
	public boolean isDailyDouble = false;
	
	public Clue( int id, String text, String answer, int value)
	{
		this.id = id;
		
		this.text = StringEscapeUtils.unescapeHtml4(text);
		this.answer = StringEscapeUtils.unescapeHtml4(answer);
		
		this.value = value;
		this.wageredValue = value;
		
	}
	
	public Clue( ResultSet rs )
	{
		try{
			
			this.id = rs.getInt( "id" );
			
			this.text = StringEscapeUtils.unescapeHtml4( rs.getString("text"));
			this.answer = StringEscapeUtils.unescapeHtml4( rs.getString("answer"));

			this.isDailyDouble = (rs.getInt("isDD") == 1);
			
			this.value = rs.getInt("value"); // if this is a daily double, we need to figure out an alternate way of determining value
			
			this.wageredValue = rs.getInt("value");
			
		}
		catch(SQLException e)
		{
			
			Logger.log( "Clue.Clue). Something bad happened");
			e.printStackTrace();
		}
		
	}
	
	// Overridden by NullClue
	public boolean isNull()
	{
		return false;
	}
	
	public String toString()
	{
		String tmp = "Clue #" + this.id + " $" + this.value +  ": " + this.text + " (" + this.answer + ")" ;
		if( this.isDailyDouble)
			tmp += "(DAILY DOUBLE: $" + this.wageredValue + ")";
		return tmp;
	}
}
