package cafe.feestneus;

public class KjK_Question
{
    public int id = 0;
    public String title = "";
    public int timestamp = 0;
    public String text = "";
    public boolean answered = false;

	public int id()
	{
		return this.id;
	}

	public void id(int id)
	{
		this.id = id;
	}

	public String title()
	{
		return this.title;
	}

	public void title(String title)
	{
		this.title = title;
	}

	public int timestamp()
	{
		return this.timestamp;
	}

	public void timestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}

	public String text()
	{
		return this.text;
	}
	
	public void addText(String text)
	{
		this.text += text;
	}

	public boolean answered()
	{
		return this.answered;
	}
	
	public void answered(boolean answered)
	{
		this.answered = answered;
	}
}
