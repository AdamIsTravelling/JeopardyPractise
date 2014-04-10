
public class NullClue extends Clue
{
	public NullClue( int value )
	{
		super(0, "", "", value);
	}

	public NullClue(int id, String text, String answer, int value)
	{
		super(id, text, answer, value);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isNull()
	{
		return true;
	}
	
	public String toString()
	{
		return "Clue #XXXXX $" + this.value + " This is a Null Clue";
	}

}
