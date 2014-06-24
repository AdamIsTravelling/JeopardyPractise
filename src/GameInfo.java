import java.sql.ResultSet;
import java.sql.SQLException;

import Tools.RoundType;

/*
 * This class represents a snippet of information about a particular Game of J!, NOT the 
 * game itself (hence differentiating from the Game class, which contains the clues, etc)
 * 
 */


public class GameInfo
{
	protected Integer gameID;
	protected Integer jArchiveID;
	protected String playerOne;
	protected String playerTwo;
	protected String playerThree;
	
	// The separator for the platers
	protected String playerSep = "/";
	
	protected boolean played; 
	
	protected String gameLabel;
	
	public GameInfo()
	{
		this.gameLabel = "Game #5!";
		this.gameID = new Integer(6);
		
		this.playerOne = this.playerTwo = this.playerThree = "";
	}
	
	public GameInfo( ResultSet r ) throws SQLException
	{
		this.gameID = r.getInt( "id" );
		this.jArchiveID = r.getInt("jarchiveID");

		this.playerOne = this.getPlayerFromID( r.getInt("firstPlayer") );
		this.playerTwo = this.getPlayerFromID( r.getInt("secondPlayer") );
		this.playerThree = this.getPlayerFromID( r.getInt("thirdPlayer") );
		
		this.gameLabel = "Episode #" + this.jArchiveID + ": "+ r.getString( "playDate") ;
		this.gameLabel += " (" + playerOne + playerSep + playerTwo + playerSep + playerThree + ")"  ;
	}

	public String toString()
	{
		return this.gameLabel;
	}
	
	protected String getPlayerFromID( Integer playerID )
	{
		String retval = "";
		String query = "SELECT * FROM player WHERE id = '" + playerID + "'";
		ResultSet results = Database.runQuery( query );
		
		try
		{
			if( results.next() )
			{
				retval = results.getString("fullName");
				
			}
		} catch (SQLException e)
		{
			Logger.log(e.toString());
		}
		
		return retval;
	}
}
