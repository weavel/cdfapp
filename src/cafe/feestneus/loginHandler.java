package cafe.feestneus;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

 
public class loginHandler extends DefaultHandler
{
 
	// ===========================================================
	// Fields
	// ===========================================================

    private boolean inSession = false;
    private String sessionHash = "";

    // ===========================================================
	// Getter & Setter
	// ===========================================================
	 
    public String getParsedData()
    {
    	return this.sessionHash;
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
		if (localName.equals("session"))
		{
			this.inSession = true;
		}
	}
	   
	    /** Gets be called on closing tags like:
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		if (localName.equals("session"))
		{
	    	this.inSession = false;
		}
    }

	/** Gets be called on the following structure:
	 * <tag>characters</tag> */
	@Override
	public void characters(char ch[], int start, int length)
	{
		if(this.inSession)
		{
			this.sessionHash = new String(ch);
		}
	}
}
