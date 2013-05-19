package cafe.feestneus;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class VisitorHandler extends DefaultHandler
{
 
	// ===========================================================
	// Fields
	// ===========================================================
	       
    private boolean today = false;
    private boolean tomorrow = false;
    private boolean dayafter = false;
    private boolean inUserName = false;
   
    private VisitorsSet Visitors = new VisitorsSet();
	 
    // ===========================================================
	// Getter & Setter
	// ===========================================================
	 
    public VisitorsSet getParsedData()
    {
    	return this.Visitors;
    }
	 
    //===========================================================
	// Methods
	// ===========================================================
    @Override
    public void startDocument() throws SAXException
    {
            this.Visitors = new VisitorsSet();
    }
	 
	@Override
	public void endDocument() throws SAXException
	{
	
	}
 
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		if (localName.equals("today"))
		{
			this.today = true;
		}
		else if (localName.equals("tomorrow"))
		{
		    this.tomorrow = true;
		}
		else if (localName.equals("dayaftertomorrow"))
		{
		    this.dayafter = true;
		}
		else if (localName.equals("name"))
		{
			this.inUserName = true;
		}
		// Extract an Attribute
		//String attrValue = atts.getValue("date");
	}
	   
	    /** Gets be called on closing tags like:
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		if (localName.equals("today"))
		{
	    	this.today = false;
		}
		else if (localName.equals("tomorrow"))
		{
	        this.tomorrow = false;
	    }
		else if (localName.equals("dayaftertomorrow"))
		{
	        this.dayafter = false;
	    }
		else if (localName.equals("name"))
    	{
			this.inUserName = false;
        }
    }
	   
	    /** Gets be called on the following structure:
	 * <tag>characters</tag> */
	@Override
	public void characters(char ch[], int start, int length)
	{
		if(this.inUserName)
		{
		    if(this.today)
		    {
		    	Visitors.setExtractedUsers(new String(ch, start, length));
		    }
		    else if(this.tomorrow)
		    {
		    	Visitors.setExtractedUsersTomorrow(new String(ch, start, length));
		    }
		    else if(this.dayafter)
		    {
		    	Visitors.setExtractedUsersDayAfter(new String(ch, start, length));
		    }
		}
	}
}
