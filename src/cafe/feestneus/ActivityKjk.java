package cafe.feestneus;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityKjk extends Activity implements LocationListener
{
	private userData UserData = userData.getInstance();
	private boolean inUitleg = false;
	private double GPS_Lat = 0;
	private double GPS_Lng = 0;
	private boolean inExplanation = false;
	private int currentQuestion = 0;
	private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	private Vector<KjK_Question> questions = new Vector<KjK_Question>();
	private Timer paintTimer = new Timer();
	private Handler h = new Handler();
	private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Toast.makeText(getApplicationContext(),"onCreate", Toast.LENGTH_SHORT).show();
        
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		BroadcastReceiver mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);

    	locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        setContentView(R.layout.loader);
        if(!Functions_Kjk.checkTeamLeader(UserData.session()))
        {
        	displayHome();
        }
        else
        {
        	getQuestions(0, 0);
        }
	}

    @Override
    protected void onResume()
    {
    	super.onResume();
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, this);
    }

    @Override
    protected void onPause()
    {
    	super.onPause();
        if(ScreenReceiver.wasScreenOn)
        {
        	//locationManager.removeUpdates(this);
        }
    }

    @Override
    protected void onStop()
    {
    	super.onStop();
    	KjK_answerHolder answerHolder = KjK_answerHolder.getInstance();
    	if(currentQuestion > 0)
    	{
    		answerHolder.questionID = currentQuestion;
			EditText editText = (EditText)findViewById(R.id.answerText);
			answerHolder.questionText = editText.getText().toString();
    	}
    	else
    	{
    		answerHolder.questionID = 0;
    	}
       	locationManager.removeUpdates(this);
    }

    @Override
    protected void onDestroy()
    {
    	super.onDestroy();
       	locationManager.removeUpdates(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	currentQuestion = 0;
    	if(inUitleg)
    	{
			if(keyCode == KeyEvent.KEYCODE_BACK)
			{
				displayKjk();
			}
			return false;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
    }
    
    private KjK_Question getQuestionById(int id)
    {
    	for(int i = 0; i < questions.size(); i++)
    	{
    		if(questions.get(i).id() == id)
    		{
    			return questions.get(i);
    		}
    	}
    	return null;
    }
        
	private void displayHome()
	{
		setResult(2);
		this.finish();
	}

    private void displayKjk()
	{
    	inExplanation = false;
    	currentQuestion = 0;

    	inUitleg = false;
		setContentView(R.layout.kjk);

		Button explainBtn = (Button)findViewById(R.id.explainBtn);
		Functions.setCustomFont(getAssets(), explainBtn);
		explainBtn.setOnClickListener(	new View.OnClickListener()
										{
											public void onClick(View v)
											{
												inExplanation = true;
												displayUitleg();
											}
										});

		Button getQuestionBtn = (Button)findViewById(R.id.getQuestionBtn);
		Functions.setCustomFont(getAssets(), getQuestionBtn);
		getQuestionBtn.setOnClickListener(	new View.OnClickListener()
											{
												public void onClick(View v)
												{
													if((GPS_Lat > 0) && (GPS_Lng > 0))
													{
														getQuestions(GPS_Lat, GPS_Lng);
													}
													else
													{
														Toast.makeText(getApplicationContext(), "Geen GPS Fix", Toast.LENGTH_SHORT).show();
													}
												}
											});

		TextView kjkDescTXT = (TextView)findViewById(R.id.kjkWelcomeTXT);
		Functions.setCustomFont(getAssets(), kjkDescTXT);

		drawQuestions();
	}

    //UI Functions
    private void displayUitleg()
    {
    	inUitleg = true;
    	setContentView(R.layout.kjk_uitleg);
    	Button backBTN = (Button)findViewById(R.id.backBtn);
    	backBTN.setOnClickListener(	new View.OnClickListener()
        								{
											public void onClick(View v) {
												displayKjk();												
											}
        								});
    }

    private void storeQuestion(KjK_Question newQuestion)
    {
    	boolean questionFound = false;
    	for(int i = 0; i < questions.size(); i++)
    	{
    		KjK_Question question = questions.get(i);
    		if(question.id() == newQuestion.id())
    		{
    			questionFound = true;
    		}
    	}
    	if(!questionFound)
    	{
    		questions.add(newQuestion);
    	}
    }

    private void drawQuestions()
    {
    	Calendar GMTCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    	Long timestamp = (GMTCalendar.getTime().getTime()/1000);
    	LinearLayout questionList = (LinearLayout)findViewById(R.id.questionList);
    	questionList.removeAllViews();
    	for(int i = 0; i < questions.size(); i++)
    	{
    		KjK_Question question = questions.get(i);
    		Button btn = new Button(getApplicationContext());
    		int secondsLeft = (int) (question.timestamp() - timestamp);
    		String timeLeft = "0 min";
    		if(secondsLeft > 300)
    		{
    			timeLeft = (secondsLeft / 60) + " min";
    		}
    		else if(secondsLeft > 0)
    		{
    			timeLeft = secondsLeft + " sec";
    		}

    		btn.setId(i);
    		btn.setText(question.title() + " (" + timeLeft + ")"); //Display question # on button because of order
    		questionList.addView(btn);
        	btn.setOnClickListener(	new View.OnClickListener()
									{
										public void onClick(View v) {
											displayQuestion(questions.get(v.getId()));
										}
									});

        	Drawable img;
        	if(question.answered())
        	{
        		img = getResources().getDrawable(R.drawable.kjk_btn_vink);
        	}
        	else
        	{
        		img = getResources().getDrawable(R.drawable.kjk_btn_leeg);
        	}
        	btn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
    	}
    }

    private void displayQuestion(KjK_Question question)
    {
    	inUitleg = true;
    	currentQuestion = question.id();
    	setContentView(R.layout.kjk_question);

    	TextView questionTXT = (TextView)findViewById(R.id.questionTXT);
    	questionTXT.setText(question.text());

    	updateQuestionTime();
    	
    	if(question.answered())
    	{
    		TextView questionAnswered = (TextView)findViewById(R.id.questionAnswered);
    		questionAnswered.setVisibility(View.VISIBLE);
    	}

    	Button backBTN = (Button)findViewById(R.id.backBtn);
    	backBTN.setOnClickListener(	new View.OnClickListener()
        								{

											public void onClick(View v) {
												displayKjk();
											}
        								});

    	Button answerBTN = (Button)findViewById(R.id.answerBtn);
    	answerBTN.setOnClickListener(	new View.OnClickListener()
        								{
											public void onClick(View v) {
												EditText editText = (EditText)findViewById(R.id.answerText);
												try
												{
													new postAnswerTask().execute("" + currentQuestion, editText.getText().toString());
												}
												catch(Exception e)
												{
													updateQuestions(null, 2);
												}
											}
        								});
    }

    private void updateQuestionTime()
    {
    	KjK_Question question = getQuestionById(currentQuestion);
    	Calendar GMTCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    	Long timestamp = (GMTCalendar.getTime().getTime()/1000);
    	int secondsLeft = (int)(question.timestamp() - timestamp);
    	if(secondsLeft <= 0)
    	{
    		EditText editText = (EditText)findViewById(R.id.answerText);
    		editText.setVisibility(View.GONE);

    		Button answerBTN = (Button)findViewById(R.id.answerBtn);
    		answerBTN.setVisibility(View.GONE);

    		TextView questionExpiredTXT = (TextView)findViewById(R.id.questionExpiredTXT);
    		questionExpiredTXT.setVisibility(View.VISIBLE);
    	}

		String timeLeft = "0 min";
		if(secondsLeft > 300)
		{
			timeLeft = (secondsLeft / 60) + " min";
		}
		else if(secondsLeft > 0)
		{
			timeLeft = secondsLeft + " sec";
		}
		Button answerBtn = (Button)findViewById(R.id.answerBtn);
		answerBtn.setText(getResources().getString(R.string.BtnAnswer) + " (" + timeLeft + ")");
    }

	private class postAnswerTask extends AsyncTask<String, Void, Boolean>
	{
		@Override
		protected Boolean doInBackground(String... input)
		{
			try
			{
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("questionNr", input[0]));
				nameValuePairs.add(new BasicNameValuePair("answer", input[1]));
	 			SynchData.postData("http://app.cafedefeestneus.nl/data.php?client=app&action=openKenJeKroniek&subaction=uploadAnswer&session=" + UserData.session(), nameValuePairs);
	 			return true;
			}
			catch (Exception e)
			{
				return false;    		
			}
		}

		protected void onPostExecute(Boolean result)
		{
			if(result)
			{
				displayKjk();
				Toast.makeText(getApplicationContext(), "Antwoord gepost", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Antwoord niet gepost, kon niet verbinden met server", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void getQuestions(double lat, double lng)
	{
		try
		{
			new FetchQuestionsTask().execute(lat, lng);
		}
		catch(Exception e)
		{
			updateQuestions(null, 2);
		}
	}

	private void updateQuestions(KjK_QuestionSet QuestionSet, int state)
	{
		questions = new Vector<KjK_Question>();
		if(QuestionSet != null)
		{
			Vector<KjK_Question> newQuestions = QuestionSet.getQuestions();
			for(int i = 0; i < newQuestions.size(); i++)
			{
				storeQuestion(newQuestions.get(i));
			}
		}

    	KjK_answerHolder answerHolder = KjK_answerHolder.getInstance();
    	//Toast.makeText(getApplicationContext(), ""+answerHolder.questionID, Toast.LENGTH_LONG).show();
    	if(answerHolder.questionID > 0)
    	{
    		currentQuestion = answerHolder.questionID; 
	  		KjK_Question question = getQuestionById(currentQuestion);
    		displayQuestion(question);

			EditText editText = (EditText)findViewById(R.id.answerText);
			editText.setText(answerHolder.questionText);
    	}
    	else
    	{
    		displayKjk();
    	}

        paintTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
            	 h.post(new Runnable() {

            	        public void run() {
		            	        	if(currentQuestion > 0)
		            	        	{
		            	        		updateQuestionTime();
		            	        	}
		            	        	else if(!inExplanation)
		                        	{
		                        		drawQuestions();
		                        	}
            	                }
            	            });
            }
        }, 0, 1000);
    }

	private class FetchQuestionsTask extends AsyncTask<Double, Void, KjK_QuestionSet>
	{
		@Override
		protected KjK_QuestionSet doInBackground(Double... geo)
		{
			try
			{
				/* Create a URL we want to load some xml-data from. */
				URL url = new URL("http://app.cafedefeestneus.nl/data.php?client=app&action=openKenJeKroniek&subaction=getQuestions&lat="+geo[0]+"&lng="+geo[1]+"&session="+UserData.session());

				/* Get a SAXParser from the SAXPArserFactory. */
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();

				XMLReader xr = sp.getXMLReader();
				KjK_QuestionHandler qstnHandler = new KjK_QuestionHandler();
				xr.setContentHandler(qstnHandler);

				xr.parse(new InputSource(url.openStream()));

				KjK_QuestionSet questions = qstnHandler.getParsedData();
				return questions;
			}
			catch (Exception e)
			{
				return null;    		
			}
		}

		protected void onPostExecute (KjK_QuestionSet questions)
		{
			updateQuestions(questions, 1);
		}
	}

	public void onLocationChanged(Location location)
	{
		if((currentQuestion == 0) && (!inExplanation))
		{
			Button getQuestionBtn = (Button)findViewById(R.id.getQuestionBtn);
			TextView kjkGPSFixTXT = (TextView)findViewById(R.id.kjkGPSFixTXT);
			if(location.getAccuracy() < 30)
			{
				this.GPS_Lat = location.getLatitude();
				this.GPS_Lng = location.getLongitude();
		
				getQuestionBtn.setVisibility(View.VISIBLE);
				kjkGPSFixTXT.setVisibility(View.GONE);
			}
			else
			{
				getQuestionBtn.setVisibility(View.GONE);
				kjkGPSFixTXT.setVisibility(View.VISIBLE);
			}
		}
	}

	public void onProviderDisabled(String provider)
	{
	}

	public void onProviderEnabled(String provider)
	{
	}

	public void onStatusChanged(String provider, int status, Bundle extras)
	{
	}
}