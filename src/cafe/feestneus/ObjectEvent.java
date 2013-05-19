package cafe.feestneus;

public class ObjectEvent
{
    public int day = 0;
    public int month = 0;
    public int year = 0;
	public String eventType;
	public boolean aboValid = false;

	public ObjectEvent(int newDay, int newMonth, int newYear, String newType, boolean newAboValid)
	{
		day = newDay;
		month = newMonth;
		year = newYear;
		eventType = newType;
		aboValid = newAboValid;
	}

	public boolean isIdentical(ObjectEvent testEvent)
	{
		return ((day == testEvent.day) && (month == testEvent.month) && (year == testEvent.year));
	}
}