package cafe.feestneus;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

 
public class EventHandler extends DefaultHandler
{
    private Vector<ObjectEvent> events = new Vector<ObjectEvent>();

    public Vector<ObjectEvent> getParsedData()
    {
    	return this.events;
    }

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		if (localName.equals("event"))
		{
			boolean aboValid = (Integer.parseInt(atts.getValue("abovalid")) == 1);
			System.out.println("valid:" + atts.getValue("abovalid")+" ("+aboValid+")");
			String date = atts.getValue("date");
			String type = atts.getValue("type");

			int year = Integer.parseInt(date.substring(0, 4));
			int month = Integer.parseInt(date.substring(5, 7)) - 1;
			int day = Integer.parseInt(date.substring(8, 10));
			ObjectEvent event = new ObjectEvent(day, month, year, type, aboValid);
			events.add(event);
		}
	}
}