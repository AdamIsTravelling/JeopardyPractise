import java.sql.ResultSet;
import java.sql.SQLException;

import Tools.RoundType;

public class Game
{
	protected int id; // the database primary key - also what is used to link categories and (I think) questions
	protected int jArchiveID; // The id of the game in the jeopardy archives - 1 for the first episode, etc.
	
	protected String dateString; // a string representing the date this was first run 
	protected String remarks;
	
	protected Round singleJ;
	protected Round doubleJ;
	protected Round finalJ;
	
	protected Round currentRound;
	
	protected boolean isValidGame = true;
	protected String isInvalidBecause = "";
	
	public Game( int id )
	{
		Logger.log( "Building game #" + id);
		this.jArchiveID = id;
		
		this.singleJ = new Round( RoundType.SINGLE);
		this.doubleJ = new Round( RoundType.DOUBLE);
		this.finalJ = new Round( RoundType.FINAL );
		
		String query = "SELECT * FROM jeopardy.game WHERE jarchiveID = '" + this.jArchiveID + "'";
		ResultSet results = Database.runQuery( query );
		
		try
		{
			while( results.next() )
			{
				Logger.log( results.getString( "playDate") );
				this.dateString = results.getString( "playDate" );
				this.id = results.getInt( "id" );
						
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			this.isValidGame = false;
			e.printStackTrace();
		}
		
		if( this.isValidGame)
		{
			// then I guess we can start by constructing this game. First, grab the categories
			
			this.singleJ.loadRound(this.id, this.jArchiveID );
			
			this.doubleJ.loadRound( this.id, this.jArchiveID );
			this.finalJ.loadRound( this.id,  this.jArchiveID );
		}
		
	}
	
	public void setIsSingleJeopardy()
	{
		this.currentRound = this.doubleJ;
	}
	
	public Round getSingleJeopardy()
	{
		return this.singleJ;
	}

	public Round getDoubleJeopardy()
	{
		return this.doubleJ;
	}
	
	public Round getFinalJeopardy()
	{
		return this.finalJ;
	}
}
