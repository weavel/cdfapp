package cafe.feestneus;

import android.content.Context;
import android.content.SharedPreferences;

public class userData
{
	//Singleton
	private static userData instance;

	public static userData getInstance()
	{
		if (instance == null)
		{
			instance = new userData();
		}
		return instance;
	}

	//Class logic
	public static final String PREFS_NAME = "cafe_settings";

	private String username = "";
	private String password = "";
	private ObjectCalendar visitCalendar = new ObjectCalendar();
	private String session = null;

	public String tmpUsername = "";
	public String tmpPassword = "";
	
	public void loadSettings(Context ctx)
	{
			SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
			username = settings.getString("username", "");
			password = settings.getString("password", "");
			session = settings.getString("session", "");
	}

	public void saveSettings(Context ctx)
	{
		SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", this.username);
		editor.putString("password", this.password);
		editor.putString("session", this.session);
		editor.commit();
	}

	public String userName()
	{
		return this.username;
	}
	
	public void userName(String userName)
	{
		this.username = userName;
	}

	public String password()
	{
		return this.password;
	}

	public void password(String password)
	{
		this.password = password;
	}
	
	public String session()
	{
		return this.session;
	}
	
	public void session(String hash)
	{
		this.session = hash;
	}
	
	public void toggleDate(int year, int month, int day, boolean notify)
	{
		ObjectVisit visit = new ObjectVisit(day, month, year, notify, true);

		if(!visitCalendar.contains(visit))
		{
			visitCalendar.add(visit);
		}
		else
		{
			visitCalendar.remove(visit);
		}
	}
	
	public void setVisitCalendar(ObjectCalendar newVisitCalendar)
	{
		this.visitCalendar = newVisitCalendar;
	}
	
	public ObjectCalendar getVisitCalendar()
	{
		return this.visitCalendar;
	}
	
	public boolean loggedIn()
	{
		return (this.session != null);
	}
	
	public void logout()
	{
		this.session = null;
		this.password = "";
	}
}