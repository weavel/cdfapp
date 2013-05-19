package cafe.feestneus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityProfile extends Activity
{
	private userData UserData = userData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loader);
        if(Functions.validateLogin(UserData.session()) <= 0)
        {
        	UserData.logout();
        	displayHome();
        }
        else
        {
        	displayProfile();
        }
	}
        
	private void displayHome()
	{
		setResult(2);
		this.finish();
	}

    private void displayProfile()
	{
		setContentView(R.layout.profile);

		Button LogoutBTN = (Button)findViewById(R.id.LogoutBTN);
		LogoutBTN.setOnClickListener(	new View.OnClickListener()
										{
											public void onClick(View v)
											{
												UserData.logout();
												displayHome();
												Toast.makeText(getApplicationContext(), "U bent uitgelogd", Toast.LENGTH_LONG).show();
											}
										});

		Button backBTN = (Button)findViewById(R.id.backBTN);
        backBTN.setOnClickListener(	new View.OnClickListener()
									{
										public void onClick(View v)
										{
											displayHome();
										}
									});

        TextView profileDescTXT = (TextView)findViewById(R.id.profileDescTXT);
        
		Functions.setCustomFont(getAssets(), profileDescTXT);
		Functions.setCustomFont(getAssets(), backBTN);
		Functions.setCustomFont(getAssets(), LogoutBTN);
	}
}