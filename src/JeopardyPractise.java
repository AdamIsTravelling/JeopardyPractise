
// This class is the main "holder class for pretty much everything. It keeps Start from having to do anything beyond
// calling one of these and saying run
public class JeopardyPractise 
{
	Controller screenHolder;
	Controller menuScreen;
	
	public  JeopardyPractise()
	{
		this.screenHolder = new Controller( "This is Jeopardy!");
		this.screenHolder.init();
	}
	
	public void run()
	{
	
		this.screenHolder.gunIt();	
	}
	
}
