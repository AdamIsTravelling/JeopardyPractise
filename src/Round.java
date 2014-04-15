import java.util.ArrayList;
import java.util.List;

import Tools.RoundType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Round
{
	protected RoundType type = RoundType.SINGLE;
	protected int roundTypeInt = -1;
	
	protected Integer cluesInRound = 0;
	protected Integer cluesRemainingInRound = 0;
//	protected int gameJArchiveID;
	
	protected List<Category> categories;
	
	public Round( RoundType roundType )
	{
		
		this.type = roundType;
		// The person who wrote the script to get the data dump made some fairly suspect decisions
		switch( this.type )
		{
			case SINGLE:
				this.roundTypeInt = 0;
				break;
			case DOUBLE:
				this.roundTypeInt = 1;
				break;
			case FINAL:
				this.roundTypeInt = 2;
				break;
			default:
				this.roundTypeInt = -1;
			
		}

		this.categories = new ArrayList<Category>();
	}
	
	public void loadRound( int gameID, int jarchiveGameID )
	{
		boolean oldGameValues = (jarchiveGameID < 3966); // This was the date they changed clue values
		
		// First, load all the categories. 
		String query = "SELECT * FROM jeopardy.category WHERE game = '" + gameID + 
							"' AND round = '"+ this.roundTypeInt + "' ORDER BY boardPosition" ; 
		ResultSet results = Database.runQuery( query );
		
		try
		{
			while( results.next() )
			{
				Category tmp = new Category( results.getInt("id"), results.getString("name"), this.type, oldGameValues );
				this.categories.add(tmp);
						
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			Logger.log( "Round.loadRound(). Something bad happened");
			e.printStackTrace();
		}
		
		// Then, for each category, load the questions
		for (Category c : this.categories) {
	        c.getClues( gameID );
	        this.cluesInRound += c.questionsInRound;
	        c.printOut();
	    } 
		
		this.cluesRemainingInRound = this.cluesInRound;
		Logger.log( "Round loaded. there are " + this.cluesInRound + " questions in this round");

	}
	
	public void clueRevealed()
	{
		this.cluesRemainingInRound--;
		Logger.log( "There are currently " + this.cluesRemainingInRound + " clues remaining in this round" );
	}
	
	public boolean allCluesRevealed()
	{
		return (this.cluesRemainingInRound == 0);
	}
}
