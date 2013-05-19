package cafe.feestneus;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityForum extends Activity
{
	private userData UserData = userData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
		if(Functions.validateLogin(UserData.session()) < 1)
        {
			UserData.logout();
			setResult(-1);
			this.finish();
        }
		else
		{
			setContentView(R.layout.loader);
			new ForumTask().execute("", "", "");
		}
	}

	private void displayHome()
	{
		setResult(3);
		this.finish();
	}
	
	private void displayForum()
	{
		setContentView(R.layout.forum);
		Button backBTN = (Button)findViewById(R.id.backBTN);
        backBTN.setOnClickListener(	new View.OnClickListener()
        								{
											public void onClick(View v) {
												CharSequence contentTitle = "Café de Feestneus";
												CharSequence contentText = "Er zijn nieuwe forumberichten";
												CafeNotificationManager NotificationMngr = (CafeNotificationManager)getApplication();
												NotificationMngr.pushNotification(contentTitle, contentText);
												/*
											    Timer timer = new Timer();
								                TimerTask timerTask = new TimerTask()
								                {
								                    @Override
								                    public void run()
								                    {
														CharSequence contentTitle = "Café de Feestneus";
														CharSequence contentText = "Er zijn nieuwe forumberichten";
														NotificationManager  NotificationMngr = (NotificationManager)getApplication();
														NotificationMngr.pushNotification(contentTitle, contentText);
								                    }
								                };
								                timer.schedule(timerTask,  5000, 15000);
								                */
												
    											displayHome();												
											}
        								});

        Functions.setCustomFont(getAssets(), backBTN);
	}

	private class ForumTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... navigationInfo)
		{
			/*
            try
    		{
            	XMLRPCClient client = new XMLRPCClient("http://cafedefeestneus.nl/mobiquo/mobiquo.php");
                Object[] response = (Object[])client.call("get_forum");
                for(int i = 0; i < response.length; i++)
                {
                	Toast.makeText(getApplicationContext(), (String)response[i].toString(), Toast.LENGTH_LONG).show();
                }
    		}
    		catch (XMLRPCException e)
    		{
    		}
            */
	    	return "";
		}

		protected void onPostExecute (String sessionHash)
		{
			displayForum();
		}
	}
}