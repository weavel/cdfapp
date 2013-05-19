package cafe.feestneus;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class Functions_Kjk
{
	public static boolean checkTeamLeader(String sessionId)
	{
    	try
    	{
	        /* Create a URL we want to load some xml-data from. */
	        URL url = new URL("http://app.cafedefeestneus.nl/data.php?client=app&action=openKenJeKroniek&subaction=checkTeamLeader&session=" + sessionId);

	        /* Get a SAXParser from the SAXPArserFactory. */
	        SAXParserFactory spf = SAXParserFactory.newInstance();
	        SAXParser sp = spf.newSAXParser();
	
	        XMLReader xr = sp.getXMLReader();
	        HandlerSessionValidation handler = new HandlerSessionValidation();
	        xr.setContentHandler(handler);
	       
	        xr.parse(new InputSource(url.openStream()));
	
	        return (handler.getParsedData() > 0);
    	}
    	catch (Exception e)
    	{
    	}
    	return false;
	}
}
