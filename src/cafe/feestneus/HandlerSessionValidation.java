package cafe.feestneus;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

 
public class HandlerSessionValidation extends DefaultHandler
{
 
	// ===========================================================
	// Fields
	// ===========================================================
	       
    private boolean inUserId = false;
    private int userId = -1;
   
    // ===========================================================
	// Getter & Setter
	// ===========================================================
	 
    public int getParsedData()
    {
    	return this.userId;
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
		if (localName.equals("uId"))
		{
			this.inUserId = true;
		}
	}
	   
	    /** Gets be called on closing tags like:
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		if (localName.equals("uId"))
		{
	    	this.inUserId = false;
		}
    }

	/** Gets be called on the following structure:
	 * <tag>characters</tag> */
	@Override
	public void characters(char ch[], int start, int length)
	{
		if(this.inUserId)
		{
			this.userId = Integer.parseInt(new String(ch, start, length));
		}
	}
}
