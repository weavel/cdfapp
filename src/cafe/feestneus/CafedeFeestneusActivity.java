package cafe.feestneus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CafedeFeestneusActivity extends Activity
{
	//Global data
	private userData UserData = userData.getInstance();
	protected Dialog mSplashDialog;
	private boolean inCredits = false;

	public static final String PREFS_NAME = "cafe_settings";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen);

        UserData.loadSettings(getApplicationContext());

        StateSaver data = (StateSaver)getLastNonConfigurationInstance();
        if (data != null)
        {
            // Show splash screen if still loading
            if(data.showSplashScreen)
            {
                showSplashScreen();
            }
            // Rebuild your UI with your saved state here
        }
        else
        {
            showSplashScreen();
            // Do your heavy loading here on a background thread
        }
        int i = 0;
        int validated = -1;

        displayLoader();
        //if(Functions.serverCheck())
        if(true)
        {
	        while((i < 4) && (validated < 1))
	        {
	        	validated = Functions.validateLogin(UserData.session());
	        	i++;
	        }
	    }
        if(validated < 0)
        {
        	Toast.makeText(getApplicationContext(), "Fout: Kon geen verbinding maken met de server", Toast.LENGTH_LONG).show();
        }
        if(validated == 0)
        {
        	UserData.logout();
        }
        displayHome();
    }

    @Override
    protected void onStop()
    {
    	super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if(inCredits)
    	{
			if(keyCode == KeyEvent.KEYCODE_BACK)
			{
				displayHome();
			}
			return false;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
    }

    //UI Functions
    private void displayHome()
    {
    	inCredits = false;
    	if(UserData.loggedIn())
		{
	    	setContentView(R.layout.main);
	    	Button forumBTN = (Button)findViewById(R.id.forumBTN);
	    	forumBTN.setOnClickListener(	new View.OnClickListener()
	        								{
	
												public void onClick(View v) {
													displayForum();												
												}
	        								});
	
	    	Button wieinParkBTN = (Button)findViewById(R.id.wieinParkBTN);
	    	wieinParkBTN.setOnClickListener(	new View.OnClickListener()
	        								{
	
												public void onClick(View v) {
													displayLoader();
													displayWieinPark();												
												}
	        								});
	    	Button kenJeKroniekBtn = (Button)findViewById(R.id.kenJeKroniekBtn);

    		if(Functions_Kjk.checkTeamLeader(UserData.session()))
	    	{
	    		kenJeKroniekBtn.setOnClickListener(	new View.OnClickListener()
			        								{
			
														public void onClick(View v) {
															displayKenJeKroniek();												
														}
			        								});
	    		kenJeKroniekBtn.setVisibility(0);
	    	}
	    	else
	    	{
	    		kenJeKroniekBtn.setVisibility(8);
	    	}
	
	    	Button profileBtn = (Button)findViewById(R.id.profileBtn);
	    	profileBtn.setOnClickListener(	new View.OnClickListener()
            								{
    											public void onClick(View v)
    											{
    												displayProfile();												
    											}
            								});

	    	TextView cafeLinkTXT = (TextView)findViewById(R.id.cafeLinkTXT);
	    	cafeLinkTXT.setOnClickListener(	new View.OnClickListener()
	        								{
	
												public void onClick(View v) {
													Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cafedefeestneus.nl"));
											    	startActivity(browserIntent);
												}
	        								});

	    	ImageView TwitterLogo = (ImageView)findViewById(R.id.TwitterLogo);
	    	TwitterLogo.setOnClickListener(	new View.OnClickListener()
	        								{
	
												public void onClick(View v) {
													Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/CafeDeFeestneus"));
											    	startActivity(browserIntent);
												}
	        								});

	    	ImageView FacebookLogo = (ImageView)findViewById(R.id.FacebookLogo);
	    	FacebookLogo.setOnClickListener(	new View.OnClickListener()
		        								{
		
													public void onClick(View v) {
														Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/CafeDeFeestneus"));
												    	startActivity(browserIntent);
													}
		        								});

	    	//Set fonts
	    	TextView welcomeTXT = (TextView)findViewById(R.id.welcomeTXT);
	    	Functions.setCustomFont(getAssets(), cafeLinkTXT);
	    	Functions.setCustomFont(getAssets(), welcomeTXT);
	    	Functions.setCustomFont(getAssets(), forumBTN);
	    	Functions.setCustomFont(getAssets(), wieinParkBTN);
	    	Functions.setCustomFont(getAssets(), kenJeKroniekBtn);
	    	Functions.setCustomFont(getAssets(), profileBtn);
		}
		else
		{
			displayLogin();
			Toast.makeText(getApplicationContext(), "Om deze app te gebruiken moet je ingelogd zijn.", Toast.LENGTH_LONG).show();
		}
    }

    private void displayLoader()
	{
		setContentView(R.layout.loader);
	}

	private void displayLogin()
	{
		Intent loginIntent = new Intent(CafedeFeestneusActivity.this, ActivityLogin.class);
		CafedeFeestneusActivity.this.startActivityForResult(loginIntent, 1);
	}

    private void displayProfile()
    {
    	Intent profileIntent = new Intent(CafedeFeestneusActivity.this, ActivityProfile.class);
		CafedeFeestneusActivity.this.startActivityForResult(profileIntent, 2);
    }

    private void displayKenJeKroniek()
    {
    	Intent kjkIntent = new Intent(CafedeFeestneusActivity.this, ActivityKjk.class);
		CafedeFeestneusActivity.this.startActivityForResult(kjkIntent, 5);
    }

	private void displayForum()
	{
    	Intent forumIntent = new Intent(CafedeFeestneusActivity.this, ActivityForum.class);
		CafedeFeestneusActivity.this.startActivityForResult(forumIntent, 3);
	}

	private void displayWieinPark()
	{
    	Intent whoInParkIntent = new Intent(CafedeFeestneusActivity.this, ActivityWhoInPark.class);
		CafedeFeestneusActivity.this.startActivityForResult(whoInParkIntent, 4);
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
		if(resultCode == -1)
    	{
			finish();    	
    	}
		else
		{
			if(Functions.validateLogin(UserData.session()) < 1)
	        {
	        	UserData.logout();
	        }
			if(UserData.loggedIn())
			{
	    		if(resultCode > 0)
		    	{
		    		switch(resultCode)
		    		{
			    		case 5:
			    			displayLogin();
			    			break;
		    			default:
		    				displayHome();
		    				break;
		    		}
		    	}
	    		else
	    		{
	    			displayHome();
	    		}
			}
			else
			{
				displayLogin();
			}
		}
    }

	@Override
	public Object onRetainNonConfigurationInstance()
	{
	    StateSaver data = new StateSaver();

	    if(mSplashDialog != null)
	    {
	        data.showSplashScreen = true;
	        removeSplashScreen();
	    }
	    return data;
	}

	/**
	 * Removes the Dialog that displays the splash screen
	 */
	protected void removeSplashScreen()
	{
	    if (mSplashDialog != null)
	    {
	        mSplashDialog.dismiss();
	        mSplashDialog = null;
	    }
	}

	/**
	 * Shows the splash screen over the full Activity
	 */
	protected void showSplashScreen()
	{
/*
		getSupportActionBar()
		ActionBar actionBar = getActionBar();
		actionBar.hide();
*/

	    mSplashDialog = new Dialog(this, R.style.SplashScreen);
	    mSplashDialog.setContentView(R.layout.splashscreen);
	    mSplashDialog.setCancelable(false);
	    mSplashDialog.show();
	 
	    // Set Runnable to remove splash screen just in case
	    final Handler handler = new Handler();
	    handler.postDelayed(	new Runnable()
							    {
	    							public void run()
	    							{

	    								removeSplashScreen();
	    							}
							    },
							    3500);
	}
	 
	/**
	 * Simple class for storing important data across config changes
	 */
	private class StateSaver
	{
	    public boolean showSplashScreen = false;
	}
}