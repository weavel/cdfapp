package cafe.feestneus;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityWhoInParkForm extends ActivityBase
{
	private userData UserData = userData.getInstance();

	//Wieinpark data
	private int curDay = 0;
	private int curMonth = 0;
	private int curYear = 0;
	private boolean customDate = false;
	private int firstUsedSquare = 0;
	private int lastUsedSquare = 0;

	private Point calenderDragStart = new Point();  
	private boolean calenderDrag = false;
	private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        displayLoader();
        
		if(Functions.validateLogin(UserData.session()) <= 0)
        {
			UserData.logout();
			this.finish();
        }
		else
		{
			displayWieinParkForm();
		}
    }

    private void displayLoader()
	{
		setContentView(R.layout.loader);
	}
    
	private void displayWieinPark()
	{
		displayLoader();
		updateVisits();
		setResult(4);
		this.finish();
	}

	private void displayWieinParkForm()
	{
		loadVisits();
	}

	private void updateVisits()
	{
		Vector<ObjectVisit> visits = UserData.getVisitCalendar().getVisits();
		nameValuePairs = new ArrayList<NameValuePair>(2);
		int a = 0;
		for(int i = 0; i < visits.size(); i++)
		{
			ObjectVisit visit = visits.get(i);
			if(visit.edited)
			{
				String visitDate = visit.year+"-" + (visit.month + 1) + "-"+visit.day;
				nameValuePairs.add(new BasicNameValuePair("visit_" + a, visitDate + ":" + (visit.removed ? '1' : '0')));
				a++;
			}
		}

		if(a > 0)
		{
			nameValuePairs.add(new BasicNameValuePair("numVisits", "" + a));
			nameValuePairs.add(new BasicNameValuePair("updateMethod", "2"));

			Runnable r = new Runnable()
			{
				public void run() 
				{
					SynchData.postData("http://app.cafedefeestneus.nl/data.php?client=app&action=synchPersonalVisits&session=" + UserData.session(), nameValuePairs);
					return ;
				}
			};
			Thread thread = new Thread(r);
			thread.start();
		}
	}

	private void initInterface()
	{
		if(!this.customDate)
		{
			Calendar calender = Calendar.getInstance();
			curDay = calender.get(Calendar.DAY_OF_MONTH);
			if(curDay == 1)
			{
				curDay = 8;
			}
			curDay--;
			curMonth = calender.get(Calendar.MONTH);
			curYear = calender.get(Calendar.YEAR);
		}
		
		setContentView(R.layout.wieinparkform);
		Button backBTN = (Button)findViewById(R.id.backBTN);
        backBTN.setOnClickListener(	new View.OnClickListener()
        								{
											public void onClick(View v)
											{
												displayWieinPark();
											}
        								});
		ImageView calenderView = (ImageView)findViewById(R.id.calenderView);
		calenderView.setOnClickListener(	new View.OnClickListener()
											{
												public void onClick(View v)
												{
												}
											});
		calenderView.setOnTouchListener(	new View.OnTouchListener()
											{
												public boolean onTouch(View v, MotionEvent event)
												{
													if(event.getAction() == MotionEvent.ACTION_DOWN)
													{
														calenderDragStart.x = event.getX();
														calenderDragStart.y = event.getY();
														calenderDrag = false;
													}
													else if(event.getAction() == MotionEvent.ACTION_MOVE)
													{
														if(Math.abs(calenderDragStart.x - event.getX()) > 10)
														{
															calenderDrag = true;
														}
														if(Math.abs(calenderDragStart.y - event.getY()) > 10)
														{
															calenderDrag = true;
														}
													}
													else if(event.getAction() == MotionEvent.ACTION_UP)
													{
														if(!calenderDrag)
														{
															int column = getCalenderXIndex(v.getWidth(), event.getX());
															int row = getCalenderYIndex(v.getHeight(), event.getY());
															if((column >= 0) && (row >= 0))
															{
																if((column >= firstUsedSquare) || (row >= 1))
																{
																	if(
																		(row < 4)
																		||
																		((row == 4) && ((column <= lastUsedSquare) || (lastUsedSquare < 2)))
																		||
																		((row == 5) && (lastUsedSquare < 3) && (column <= lastUsedSquare))
																	)
																	{
																		int cellDay = 1;
																		if(firstUsedSquare == 0)
																		{
																			cellDay = ((row - 1) * 7) + column + 1;
																		}
																		else
																		{
																			cellDay = (((row * 7) + 1 + column) - firstUsedSquare);
																		}
																		UserData.toggleDate(curYear, curMonth, cellDay, false);
																		updateCalender();
																		updateVisits();
																	}
																}
															}
														}
													}
													return false;
												}
											});

		Button nextBtn = (Button)findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(		new View.OnClickListener()
										{
											public void onClick(View v)
											{
												curMonth++;
												if(curMonth > 11)
												{
													curMonth = 0;
													curYear++;
												}
												updateCalender();
											}
										});
		Button prevBtn = (Button)findViewById(R.id.prevBtn);
		prevBtn.setOnClickListener(		new View.OnClickListener()
										{
											public void onClick(View v)
											{
												curMonth--;
												if(curMonth < 0)
												{
													curMonth = 11;
													curYear--;
												}
												updateCalender();
											}
										});

		TextView wieInParkFormTXT = (TextView)findViewById(R.id.wieInParkFormTXT);
		TextView dateTxt = (TextView)findViewById(R.id.dateTxt);
		Functions.setCustomFont(getAssets(), wieInParkFormTXT);
		Functions.setCustomFont(getAssets(), backBTN);
		Functions.setCustomFont(getAssets(), prevBtn);
		Functions.setCustomFont(getAssets(), dateTxt);
		Functions.setCustomFont(getAssets(), nextBtn);

		updateCalender();
	}

	private void updateCalender()
	{
		//Get all visits
		Vector<ObjectVisit> visits = UserData.getVisitCalendar().getVisits();
		
		//Retrieve interface controlls
		TextView dateTxt = (TextView)findViewById(R.id.dateTxt);
		ImageView calenderView = (ImageView)findViewById(R.id.calenderView);
		String[] Months = getResources().getStringArray(R.array.Months);

		//Start constructing bitmap
		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.calender).copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas();
		canvas.setBitmap(mBitmap);
		Bitmap vinkBMP = BitmapFactory.decodeResource(getResources(), R.drawable.calendervink);

		float xratio = ((float)mBitmap.getWidth() / 7); //Height divided by columns
		float yratio = ((float)mBitmap.getHeight() / 6); //Height divided by dpi times rows

		//Custom Font
		//Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf");

		//Paint for text
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTypeface(Typeface.SERIF);
		paint.setAntiAlias(true);
		paint.setARGB(255, 68, 68, 68);
		paint.setTextSize(26);
		paint.setTextAlign(Paint.Align.CENTER);
		//paint.setTypeface(tf);
		
		Paint smallPaint = new Paint(paint);
		smallPaint.setTextSize(10);
		smallPaint.setFakeBoldText(true);
		smallPaint.setTextAlign(Paint.Align.LEFT);

		//Get start of month (mon = 0, tue = 2, wed = 3...)
		Calendar tmpCalender = Calendar.getInstance();
		tmpCalender.set(curYear, curMonth, 1);
		int startDay = tmpCalender.get(Calendar.DAY_OF_WEEK);
		if(startDay == 1)
		{
			startDay = 8;
		}
		startDay-=2;
		
		//Move one month forward and then one millisecond back to get last day of month
		tmpCalender.add(Calendar.MONTH, 1);
		tmpCalender.set(Calendar.DAY_OF_MONTH, 1);
		tmpCalender.set(Calendar.HOUR_OF_DAY, 0);
		tmpCalender.set(Calendar.MINUTE, 0);
		tmpCalender.set(Calendar.SECOND, 0);
		tmpCalender.set(Calendar.MILLISECOND, 0);
		tmpCalender.add(Calendar.MILLISECOND, -1);
		int daysInMonth = tmpCalender.get(Calendar.DAY_OF_MONTH);

		//Start drawing calendar
		int dayOfMonth = -1;
		int yOffset = 0;
		for(int iLine = 0; iLine < 6; iLine++)
		{
			for(int iDayOfWeek = 0; iDayOfWeek < 7; iDayOfWeek++)
			{
				//If day of week is equal to first day of month (and not already passed)
				if((dayOfMonth == -1) && (iDayOfWeek == startDay))
				{
					//First week-line will not be used to keep usable dates vertically centered
					if((iDayOfWeek == 0) && (iLine == 0))
					{
						yOffset = 1;
					}
					if(!((iDayOfWeek == 0) && (iLine == 0)))
					{
						dayOfMonth = 1;
						firstUsedSquare = iDayOfWeek;
					}
				}

				//If month has started and not ended yet
				if((dayOfMonth > 0) && (dayOfMonth <= daysInMonth))
				{
					int base_x = (int)(iDayOfWeek * xratio);
					int base_y = (int)(iLine * yratio);
					int center_x = (int)((iDayOfWeek * xratio) + (0.5 * xratio));
					int center_y = (int)((iLine * yratio) + (0.5 * xratio));
					int boxMargin = (int)FloatMath.ceil(xratio / 45);

					String dayOfMonthStr = "" + dayOfMonth;
				    Rect bounds = new Rect();
				    paint.getTextBounds(dayOfMonthStr, 0, dayOfMonthStr.length(), bounds);
					canvas.drawText(dayOfMonthStr, center_x, center_y + (bounds.bottom - bounds.top), paint);

					ObjectEvent testEvent = new ObjectEvent(dayOfMonth, curMonth, curYear, "", false);
					String eventText = UserData.getVisitCalendar().getEventType(testEvent);
					if(UserData.getVisitCalendar().getAboValid(testEvent))
					{
						smallPaint.setColor(0xff29A70D);
					}
					else
					{
						smallPaint.setColor(Color.RED);
					}

				    bounds = new Rect();
				    smallPaint.getTextBounds(eventText, 0, eventText.length(), bounds);
					canvas.drawText(eventText, base_x + boxMargin, base_y + (bounds.bottom - bounds.top) + boxMargin, smallPaint);

					if(dayOfMonth == daysInMonth)
					{
						lastUsedSquare = iDayOfWeek;
						dayOfMonth++;
					}
					dayOfMonth++;
				}
			}
		}

		//Draw visits on calendar
		for(int i = 0; i < visits.size(); i++)
		{
			ObjectVisit item = visits.get(i);
			if(!item.removed)
			{
				if((item.year == curYear) && (item.month == curMonth))
				{
					int cellDay = item.day;
					int indexY = (int)Math.floor((cellDay + firstUsedSquare - 1) / 7);
					int indexX = ((cellDay - 1) + firstUsedSquare) - (indexY * 7);
					int x = (int)(indexX * xratio);
					int y = (int)((indexY + yOffset) * yratio);
					canvas.drawBitmap(vinkBMP, x, y, null);
				}
			}
		}
		dateTxt.setText(curYear+" - "+Months[curMonth]);
		calenderView.setImageBitmap(mBitmap);
	}
	
	private int getCalenderXIndex(int totalWidth, float x)
	{
		float tileSize = (totalWidth / 7);
		return (int)FloatMath.floor(x / tileSize);
	}

	private int getCalenderYIndex(int totalHeight, float y)
	{
		float tileSize = (totalHeight / 6);
		return (int)FloatMath.floor(y / tileSize);
	}

    //Data functions
    private void loadVisits()
	{
		try
		{
			new FetchVisitsTask().execute();
		}
		catch(Exception e)
		{
			displayWieinPark();
		}
	}

    private void loadEvents()
	{
		try
		{
			new FetchEventsTask().execute();
		}
		catch(Exception e)
		{
			updateCalender();
		}
	}

    //XML Tasks
	private class FetchVisitsTask extends AsyncTask<Void, Void, Vector<ObjectVisit>>
	{
		@Override
		protected Vector<ObjectVisit> doInBackground(Void... params)
		{
	    	try
	    	{
		        /* Create a URL we want to load some xml-data from. */
		        URL url = new URL("http://app.cafedefeestneus.nl/data.php?client=app&action=getPersonalVisits&session=" + UserData.session());

		        /* Get a SAXParser from the SAXPArserFactory. */
		        SAXParserFactory spf = SAXParserFactory.newInstance();
		        SAXParser sp = spf.newSAXParser();
		
		        XMLReader xr = sp.getXMLReader();
		        VisitHandler vstHandler = new VisitHandler();
		        xr.setContentHandler(vstHandler);
		       
		        xr.parse(new InputSource(url.openStream()));
		
		        Vector<ObjectVisit> Visits = vstHandler.getParsedData();
		        return Visits;
	    	}
	    	catch (Exception e)
	    	{
	    		return null;    		
	    	} 
		}

		protected void onPostExecute (Vector<ObjectVisit> visits)
		{
			if(visits != null)
			{
				UserData.getVisitCalendar().setVisits(visits);
			}
			loadEvents();
		}
	}

    //XML Tasks
	private class FetchEventsTask extends AsyncTask<Void, Void, Vector<ObjectEvent>>
	{
		@Override
		protected Vector<ObjectEvent> doInBackground(Void... params)
		{
	    	try
	    	{
		        /* Create a URL we want to load some xml-data from. */
		        URL url = new URL("http://app.cafedefeestneus.nl/data.php?client=app&action=getEvents");

		        /* Get a SAXParser from the SAXPArserFactory. */
		        SAXParserFactory spf = SAXParserFactory.newInstance();
		        SAXParser sp = spf.newSAXParser();

		        XMLReader xr = sp.getXMLReader();
		        EventHandler evtHandler = new EventHandler();
		        xr.setContentHandler(evtHandler);

		        xr.parse(new InputSource(url.openStream()));

		        Vector<ObjectEvent> Events = evtHandler.getParsedData();
		        return Events;
	    	}
	    	catch (Exception e)
	    	{
	    		return null;    		
	    	} 
		}

		protected void onPostExecute (Vector<ObjectEvent> events)
		{
			UserData.getVisitCalendar().setEvents(events);
			initInterface();
		}
	}
}