package cafe.feestneus;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

 
public class VisitHandler extends DefaultHandler
{
 
	// ===========================================================
	// Fields
	// ===========================================================
	       
    private boolean inDate = false;
    private boolean notify = false;
   
    private Vector<ObjectVisit> visits = new Vector<ObjectVisit>();
	 
    // ===========================================================
	// Getter & Setter
	// ===========================================================
	 
    public Vector<ObjectVisit> getParsedData()
    {
    	return this.visits;
    }
	 
    //===========================================================
	// Methods
	// ===========================================================
    @Override
    public void startDocument() throws SAXException
    {
    }
	 
	@Override
	public void endDocument() throws SAXException
	{
	
	}
 
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		if (localName.equals("date"))
		{
			this.inDate = true;
	    	this.notify = (atts.getValue("notify") == "yes");
		}
	}
	   
	    /** Gets be called on closing tags like:
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		if (localName.equals("date"))
		{
	    	this.inDate = false;
		}
    }

	/** Gets be called on the following structure:
	 * <tag>characters</tag> */
	@Override
	public void characters(char ch[], int start, int length)
	{
		if(this.inDate)
		{
			int year = Integer.parseInt(""+ch[0]+ch[1]+ch[2]+ch[3]);
			int month = Integer.parseInt(""+ch[5]+ch[6]) - 1;
			int day = Integer.parseInt(""+ch[8]+ch[9]);
			ObjectVisit visit = new ObjectVisit(day, month, year, notify);
			if(visit.year > 0)
			{
				try
				{
					visits.add(visit);
				}
				catch (Exception e)
				{
				}
			}
		}
		notify = false;
	}
}
