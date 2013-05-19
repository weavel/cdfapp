package cafe.feestneus;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.res.AssetManager;
import android.widget.Button;
import android.widget.TextView;

public class Functions
{
	public static void setCustomFont(AssetManager assets, TextView view)
	{
    	//Typeface tf = Typeface.createFromAsset(assets, "fonts/font1.ttf");
    	//view.setTypeface(tf);
	}

	public static void setCustomFont(AssetManager assets, Button view)
	{
    	//Typeface tf = Typeface.createFromAsset(assets, "fonts/font1bold.ttf");
    	//view.setTypeface(tf);
	}

	public static String MD5_Hash(String s)
	{
        MessageDigest m = null;

        try
        {
        	m = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
        	e.printStackTrace();
        }

        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
	}

    public static String getLeading(int number, int length)
    {
    	String str = ""+number;
    	while(str.length() < length)
    	{
    		str = "0"+str;
    	}
    	return str;
    }
    
    public static boolean serverCheck()
	{
    	try
    	{
	        /* Create a URL we want to load some xml-data from. */
	        URL url = new URL("http://app.cafedefeestneus.nl/data.php?client=app&action=serverCheck");

	        /* Get a SAXParser from the SAXPArserFactory. */
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	
	        XMLReader xr = sp.getXMLReader();
	        HandlerSessionValidation handler = new HandlerSessionValidation();
	        xr.setContentHandler(handler);
	       
	        xr.parse(new InputSource(url.openStream()));
	
	        return (handler.getParsedData() == 42);
    	}
    	catch (Exception e)
    	{
    		return false;
    	}
	}

    public static int validateLogin(String sessionId)
	{
    	try
    	{
	        /* Create a URL we want to load some xml-data from. */
	        URL url = new URL("http://app.cafedefeestneus.nl/data.php?client=app&action=validateSession&session=" + sessionId);

	        /* Get a SAXParser from the SAXPArserFactory. */
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	
	        XMLReader xr = sp.getXMLReader();
	        HandlerSessionValidation handler = new HandlerSessionValidation();
	        xr.setContentHandler(handler);
	       
	        xr.parse(new InputSource(url.openStream()));
	
	        return ((handler.getParsedData() > 0) ? 1 : 0);
    	}
    	catch (Exception e)
    	{
    		return -1;
    	}
	}
}
