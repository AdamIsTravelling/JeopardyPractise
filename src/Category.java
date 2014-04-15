import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import Tools.RoundType;

public class Category
{
	protected int id; // the primary key of the categorytable
	protected String name;
	protected Integer questionsInRound;
	protected RoundType type = RoundType.SINGLE;
	protected boolean isOldGame = false;
	
	protected static final List<Integer> SINGLE_CLUE_VALUES = Arrays.asList( 200, 400, 600, 800, 1000);
	protected static final List<Integer> DOUBLE_CLUE_VALUES = Arrays.asList( 400, 800, 1200, 1600, 2000);
	protected static final List<Integer> HALF_CLUE_VALUES = Arrays.asList( 100, 200, 300, 400, 500);
	
	protected List<Integer> clueValues;
	protected List<Clue> clues;
	
	protected Clue dailyDoubleHolder = null;
	
	public Category( int id, String name, RoundType type, boolean isOldGame)
	{
		this.name = name;
		this.id = id;
		this.questionsInRound = 0;
		this.type = type;
		
		Logger.log ( "Creating new Category - " + name);
		this.clues = new ArrayList<Clue>();
		
		this.isOldGame = isOldGame;
		this.initClueValuesArray();
	}
	
	protected void initClueValuesArray()
	{
		if(this.isOldGame && type == RoundType.SINGLE)
			this.clueValues = HALF_CLUE_VALUES;
		else if( !this.isOldGame && type == RoundType.DOUBLE)
			this.clueValues = DOUBLE_CLUE_VALUES;
		else
			this.clueValues = SINGLE_CLUE_VALUES;
	}
	
	public void getClues( int gameID )
	{		
		// Initialise the blank clues, in order
		for (Integer val : this.clueValues) {
			this.clues.add( new NullClue( val ) );
		}
		
		
		String query = "SELECT * FROM jeopardy.clue WHERE game = '" + gameID + 
				"' AND category = '" + this.id + "' ORDER BY value ASC" ; 
		ResultSet results = Database.runQuery( query );

		try
		{
			while( results.next() )
			{
				
				Clue tmp = new Clue( results );
				this.questionsInRound++;
				if( !tmp.isDailyDouble )
				{
					int clueIndex = this.clueValues.indexOf( tmp.value );
					this.clues.set(clueIndex, tmp);
				}
				else
				{
					this.dailyDoubleHolder = tmp;
					// add the the temporary DD pile
				}
					
				
			}
		} catch (SQLException e)
		{
			
			Logger.log( "Category.loadRound(). Something bad happened");
			e.printStackTrace();
		}
		
		//now if there is a DD clue, insert it in the first empty spot in the category
		// If the category has been fully answered, then there should only be one spot remaining
		//If the category hasn't been fully answered, then it doesn't really matter where the DD is put
		if( this.dailyDoubleHolder != null )
		{
			for (int i = 0; i < this.clues.size(); i++) 
			{
				if(this.clues.get(i).getClass().getName() == "NullClue")
				{
					this.dailyDoubleHolder.value = this.clueValues.get(i);
					this.clues.set( i, this.dailyDoubleHolder);
					this.dailyDoubleHolder = null;
					break; // and then stop looking
				}
			}
		}
	}

	public Integer getQuestionsInRound()
	{
		return this.questionsInRound;
	}
	
	public void printOut()
	{
		Logger.log( "Category - "+ this.name );
		for (Clue c : this.clues) {
			Logger.log( "	" + c.toString() );
		}
		
	}
}
