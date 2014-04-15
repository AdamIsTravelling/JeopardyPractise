import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

// here's a basic database class for all to use
public class Database
{
	protected static final String dbHost = "127.0.0.1";
	protected static final String dbPort = "3306";
	protected static final String dbUser = "Adam";
	protected static final String dbPass = "RottenSound";
	protected static final String dbName = "jeopardy";
	
	protected static String connString = "";
	protected static Connection conn;
	
	protected static boolean triedConnection = false;
	protected static boolean failedConnection = false;
	
	public static void initDB()
	{
		Database.connString = Database.getConnectionString();
		try {
			Logger.log( "Attempting to connect to " + Database.connString );
			Database.triedConnection = true;
			
			Class.forName("com.mysql.jdbc.Driver");
			Database.conn = DriverManager.getConnection( Database.connString, Database.dbUser, Database.dbPass );
			Logger.log( "Connected!" );
		}
		catch( Exception e)
		{
			// what ever
			Logger.log( "Unable to connect to " + Database.connString + ":" + e.toString() );
		}
	}
	
	protected static String getConnectionString()
	{
		String retval = "";
		
		retval = "jdbc:" + "mysql://" + Database.dbHost + ":" 
				+ Database.dbPort + "/" + Database.dbName;
		
						
		return retval;
	}
	
	public static Connection getConnection()
	{
		return Database.conn;
	}
	
	public static ResultSet runQuery( String query )
	{
		ResultSet rs = null;
		Logger.log( "Running query - " + query  );
		
		try
		{
			Statement statement = Database.conn.createStatement();
			rs = statement.executeQuery( query );
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public static boolean runUpdate( String query )
	{
		Statement statement = null;
		boolean retval = false;
		Logger.log( "Running update - " + query );
		try
		{
			statement = Database.conn.createStatement();
			statement.execute( query );
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (statement != null)
			{
				try
				{
					statement.close();
				} 
				catch (SQLException e)
				{
					Logger.log( "Exception! Couldn't close update statement");
				}
			}
		}
		
		return true;
	}
}
