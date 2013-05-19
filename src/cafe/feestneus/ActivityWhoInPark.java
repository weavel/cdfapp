package cafe.feestneus;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityWhoInPark extends ActivityBase
{
	private userData UserData = userData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

		if(Functions.validateLogin(UserData.session()) <= 0)
        {
			UserData.logout();
			this.finish();
        }
		else
		{
			displayWieinPark();
		}
	}

	private void displayHome()
	{
		setResult(4);
		this.finish();
	}

	private void displayWieinPark()
	{
		setContentView(R.layout.wieinpark);
		Button backBTN = (Button)findViewById(R.id.backBTN);
        backBTN.setOnClickListener(	new View.OnClickListener()
        								{
											public void onClick(View v) {
    											displayHome();
											}
        								});
        Button setDateBTN = (Button)findViewById(R.id.setDateBTN);
        setDateBTN.setOnClickListener(	new View.OnClickListener()
										{
											public void onClick(View v) {
												displayWieinParkForm();
											}
									});
        loadUsers();

        TextView wieInParkDescTXT = (TextView)findViewById(R.id.wieInParkDescTXT);
        TextView membersTodayCaption = (TextView)findViewById(R.id.membersTodayCaption);
        TextView membersToday = (TextView)findViewById(R.id.membersToday);
        TextView membersTomorrowCaption = (TextView)findViewById(R.id.membersTomorrowCaption);
        TextView membersTomorrow = (TextView)findViewById(R.id.membersTomorrow);
        TextView membersDayAfterCaption = (TextView)findViewById(R.id.membersDayAfterCaption);
        TextView membersDayAfter = (TextView)findViewById(R.id.membersDayAfter);

        Functions.setCustomFont(getAssets(), wieInParkDescTXT);
        Functions.setCustomFont(getAssets(), membersTodayCaption);
        Functions.setCustomFont(getAssets(), membersToday);
        Functions.setCustomFont(getAssets(), membersTomorrowCaption);
        Functions.setCustomFont(getAssets(), membersTomorrow);
        Functions.setCustomFont(getAssets(), membersDayAfterCaption);
        Functions.setCustomFont(getAssets(), membersDayAfter);
        Functions.setCustomFont(getAssets(), backBTN);
        Functions.setCustomFont(getAssets(), setDateBTN);
	}
	
	private void displayWieinParkForm()
	{
		displayLoader();
		Intent calenderIntent = new Intent(ActivityWhoInPark.this, ActivityWhoInParkForm.class);
		ActivityWhoInPark.this.startActivityForResult(calenderIntent, 1);
	}

    private void displayLoader()
	{
		setContentView(R.layout.loader);
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
		if(Functions.validateLogin(UserData.session()) <= 0)
        {
			UserData.logout();
			this.finish();
        }
		else
		{
			displayWieinPark();
		}
    }
	
	private void loadUsers()
	{
		try
		{
			new FetchUserDataTask().execute();
		}
		catch(Exception e)
		{
			updateUsers(null, 2);
		}
	}
    
	private void updateUsers(VisitorsSet Visitors, int state)
	{
        TextView todayText = (TextView)findViewById(R.id.membersToday);
        TextView tomorrowText = (TextView)findViewById(R.id.membersTomorrow);
        TextView dayAfterText = (TextView)findViewById(R.id.membersDayAfter);
        Button setDateBTN = (Button)findViewById(R.id.setDateBTN);
	
        setDateBTN.setVisibility(0);
        
        if(state == 0) //Busy
        {
        	
        }
		if(state == 1) //Ready
		{
			if(Visitors != null)
			{
        		todayText.setText(Visitors.getToday());
                tomorrowText.setText(Visitors.getTomorrow());
                dayAfterText.setText(Visitors.getDayAfter());

                todayText.setVisibility(0);
                tomorrowText.setVisibility(0);
                dayAfterText.setVisibility(0);

                TextView todayTextCaption = (TextView)findViewById(R.id.membersTodayCaption);
                TextView tomorrowTextCaption = (TextView)findViewById(R.id.membersTomorrowCaption);
                TextView dayAfterTextCaption = (TextView)findViewById(R.id.membersDayAfterCaption);
                todayTextCaption.setVisibility(0);
                tomorrowTextCaption.setVisibility(0);
                dayAfterTextCaption.setVisibility(0);
                setDateBTN.setVisibility(0);
			}
        }
        else //error
        {
        	todayText.setText("Resultaten konden niet opgehaald worden.");
        	todayText.setVisibility(0);
        }
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(8);
    }

	private class FetchUserDataTask extends AsyncTask<Void, Void, VisitorsSet>
	{
		@Override
		protected VisitorsSet doInBackground(Void... params)
		{
	    	try
	    	{
		        /* Create a URL we want to load some xml-data from. */
		        URL url = new URL("http://app.cafedefeestneus.nl/data.php?client=app&action=getAllVisits&session=" + UserData.session());

		        /* Get a SAXParser from the SAXPArserFactory. */
		        SAXParserFactory spf = SAXParserFactory.newInstance();
		        SAXParser sp = spf.newSAXParser();
		
		        XMLReader xr = sp.getXMLReader();
		        VisitorHandler usrHandler = new VisitorHandler();
		        xr.setContentHandler(usrHandler);
		       
		        xr.parse(new InputSource(url.openStream()));
		
		        VisitorsSet Users = usrHandler.getParsedData();
		        return Users;
	    	}
	    	catch (Exception e)
	    	{
	    		return null;    		
	    	} 
		}
		
		protected void onPostExecute (VisitorsSet Visitors)
		{
			updateUsers(Visitors, 1);
		}
	}
}