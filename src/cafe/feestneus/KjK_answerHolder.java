package cafe.feestneus;

public class KjK_answerHolder
{
	//Singleton
	private static KjK_answerHolder instance;

	public static KjK_answerHolder getInstance()
	{
		if (instance == null)
		{
			instance = new KjK_answerHolder();
		}
		return instance;
	}

	public int questionID = 0;
	public String questionText ="";
}