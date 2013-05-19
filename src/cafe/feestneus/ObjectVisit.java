package cafe.feestneus;

public class ObjectVisit
{
    public int day = 0;
    public int month = 0;
    public int year = 0;
    public boolean notify = false;
	public boolean removed = false;
    public boolean edited = false;

	public ObjectVisit(int newDay, int newMonth, int newYear, boolean newNotify, boolean newEdited)
	{
		day = newDay;
		month = newMonth;
		year = newYear;
		notify = newNotify;
		edited = newEdited;
	}

	public ObjectVisit(int newDay, int newMonth, int newYear, boolean newNotify)
	{
		day = newDay;
		month = newMonth;
		year = newYear;
		notify = newNotify;
	}

	public boolean isIdentical(ObjectVisit testVisit)
	{
		return ((day == testVisit.day) && (month == testVisit.month) && (year == testVisit.year));
	}
}