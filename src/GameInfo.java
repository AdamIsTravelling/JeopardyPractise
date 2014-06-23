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
	
	protected boolean played; 
	
	protected String gameLabel;
	
	public GameInfo()
	{
		this.gameLabel = "Game #5!";
		this.gameID = new Integer(6);
		
	}
	
	public GameInfo( ResultSet r ) throws SQLException
	{
		this.gameID = r.getInt( "id" );
		this.jArchiveID = r.getInt("jarchiveID");

		this.gameLabel = "Episode #" + this.jArchiveID + ": "+ r.getString( "playDate") ;
		
	}

	public String toString()
	{
		return this.gameLabel;
	}
}
