package cafe.feestneus;

import java.net.URL;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLogin extends ActivityBase
{
	private userData UserData = userData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        displayLogin();
	}

	private void displayHome()
	{
		setResult(1);
		this.finish();
	}

    private void displayLogin()
	{
		setContentView(R.layout.login);

		TextView userNameTXT = (TextView)findViewById(R.id.userNameTXT);
		userNameTXT.setText(UserData.userName());

		Button backBTN = (Button)findViewById(R.id.backBTN);
        backBTN.setOnClickListener(	new View.OnClickListener()
        								{
											public void onClick(View v)
											{
    											displayHome();
											}
        								});

		Button loginBTN = (Button)findViewById(R.id.loginBTN);
		loginBTN.setOnClickListener(	new View.OnClickListener()
        								{
											public void onClick(View v)
											{
												try
												{
													TextView userNameTXT = (TextView)findViewById(R.id.userNameTXT);
													TextView passwordTXT = (TextView)findViewById(R.id.passwordTXT);
													UserData.tmpUsername = userNameTXT.getText().toString();
													UserData.tmpPassword = passwordTXT.getText().toString();

													CharSequence uNameCh = userNameTXT.getText();
													CharSequence uPassCh = passwordTXT.getText();

													String uName = uNameCh.toString();
													String uPass = uPassCh.toString();

													displayLoader();

													uName = Functions.MD5_Hash(uName.toLowerCase(Locale.US));
													new LoginTask().execute(uName, uPass);
												}
												catch(Exception e)
												{
													Toast.makeText(getApplicationContext(), "Er ging iets fout bij het inloggen. Probeer het opnieuw.", Toast.LENGTH_SHORT).show();
													displayLogin();
												}
											}
        								});

		TextView loginDescTXT = (TextView)findViewById(R.id.loginDescTXT);
		TextView passwordTXT = (TextView)findViewById(R.id.passwordTXT);

		Functions.setCustomFont(getAssets(), loginDescTXT);
		Functions.setCustomFont(getAssets(), passwordTXT);		
		Functions.setCustomFont(getAssets(), userNameTXT);
		Functions.setCustomFont(getAssets(), backBTN);
		Functions.setCustomFont(getAssets(), loginBTN);
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
		if(keyCode != KeyEvent.KEYCODE_BACK)
		{
			return super.onKeyDown(keyCode, event);
		}
		else
		{
			setResult(-1);
			finish();
		}
		return false;
    }
    
    private void displayLoader()
	{
		setContentView(R.layout.loader);
	}

	private class LoginTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... credentials)
		{
	    	try
	    	{
		        /* Create a URL we want to load some xml-data from. */
		        URL url = new URL("http://app.cafedefeestneus.nl/data.php?client=app&action=getSession&OS=Android&version=" + android.os.Build.VERSION.RELEASE + "&uName=" + credentials[0] + "&uPass=" + credentials[1]);

		        /* Get a SAXParser from the SAXPArserFactory. */
		        SAXParserFactory spf = SAXParserFactory.newInstance();
		        SAXParser sp = spf.newSAXParser();
		
		        XMLReader xr = sp.getXMLReader();
		        loginHandler lgnHandler = new loginHandler();
		        xr.setContentHandler(lgnHandler);
		       
		        xr.parse(new InputSource(url.openStream()));
		
		        return lgnHandler.getParsedData();
	    	}
	    	catch (Exception e)
	    	{
	    		return null;    		
	    	} 
		}
		
		protected void onPostExecute (String sessionHash)
		{
			saveSession(sessionHash);
		}
	}
	
    private void saveSession(String hash)
	{
    	if((hash != null) && (hash != "null") && (hash != "") && (hash.length() >= 10))
    	{
    		this.UserData.session(hash);
			saveLoginData();
	    	UserData.saveSettings(getApplicationContext());
	    	Toast.makeText(getApplicationContext(), hash, Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "U bent ingelogd", Toast.LENGTH_SHORT).show();
	    	displayHome();
    	}
    	else
    	{
    		Toast.makeText(getApplicationContext(), "U heeft incorrecte gegevens ingevuld.", Toast.LENGTH_SHORT).show();
    		displayLogin();
    	}
	}

	private void saveLoginData()
	{
		UserData.userName(UserData.tmpUsername);
		UserData.password(UserData.tmpPassword);
	}
}