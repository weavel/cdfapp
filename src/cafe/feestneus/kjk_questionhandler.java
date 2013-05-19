package cafe.feestneus;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
 
public class KjK_QuestionHandler extends DefaultHandler
{
 
	// ===========================================================
	// Fields
	// ===========================================================

	private boolean inId = false;
	private boolean inTitle = false;
	private boolean inTimestamp = false;
	private boolean inQuestionBody = false;
    private boolean inQuestion = false;
    private boolean inAnswered = false;
   
    private KjK_QuestionSet questions = new KjK_QuestionSet();
    private KjK_Question currentQuestion = null;
	 
    // ===========================================================
	// Getter & Setter
	// ===========================================================
	 
    public KjK_QuestionSet getParsedData()
    {
    	return this.questions;
    }
	 
    //===========================================================
	// Methods
	// ===========================================================
    @Override
    public void startDocument() throws SAXException
    {
    	this.questions = new KjK_QuestionSet();
    }
	 
	@Override
	public void endDocument() throws SAXException
	{
	
	}
 
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		if (localName.equals("id"))
		{
			this.inId = true;
		}
		else if (localName.equals("title"))
		{
			this.inTitle = true;
		}
		else if (localName.equals("timestamp"))
		{
			this.inTimestamp = true;
		}
		else if (localName.equals("text"))
		{
		    this.inQuestionBody = true;
		}
		else if (localName.equals("question"))
		{
			this.inQuestion = true;
			currentQuestion = new KjK_Question();
		}
		else if(localName.equals("answered"))
		{
			inAnswered = true;
		}
		// Extract an Attribute
		//String attrValue = atts.getValue("date");
	}
	   
	    /** Gets be called on closing tags like:
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		if (localName.equals("id"))
		{
	    	this.inId = false;
		}
		else if (localName.equals("title"))
		{
	    	this.inTitle = false;
		}
		else if (localName.equals("timestamp"))
		{
	    	this.inTimestamp = false;
		}
		else if (localName.equals("text"))
		{
	        this.inQuestionBody = false;
	    }
		else if(localName.equals("answered"))
		{
			inAnswered = false;
		}
		else if (localName.equals("question"))
    	{
			questions.addQuestion(currentQuestion);
			this.inQuestion = false;
        }
    }
	   
	    /** Gets be called on the following structure:
	 * <tag>characters</tag> */
	@Override
	public void characters(char ch[], int start, int length)
	{
		if(this.inQuestion)
		{
		    if(this.inId)
		    {
		    	currentQuestion.id(Integer.parseInt(new String(ch, start, length)));
		    }
		    else if(this.inTitle)
		    {
		    	currentQuestion.title(new String(ch, start, length));
		    }
		    else if(this.inTimestamp)
		    {
		    	currentQuestion.timestamp(Integer.parseInt(new String(ch, start, length)));
		    }
		    else if(this.inQuestionBody)
		    {
		    	currentQuestion.addText(new String(ch, start, length));
		    }
		    else if(this.inAnswered)
		    {
		    	currentQuestion.answered((Integer.parseInt(new String(ch, start, length)) > 0));
		    }
		}
	}
}
