package cafe.feestneus;

import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

class XMLRPCCommon {

	protected XmlSerializer serializer;
	protected XMLRPCISerializer iXMLRPCSerializer;
	
	XMLRPCCommon() {
		serializer = Xml.newSerializer();
		iXMLRPCSerializer = new XMLRPCSerializer();
	}

	/**
	 * Sets custom IXMLRPCSerializer serializer (in case when server doesn't support
	 * standard XMLRPC protocol)
	 * 
	 * @param serializer custom serializer
	 */
	public void setSerializer(XMLRPCISerializer serializer) {
		iXMLRPCSerializer = serializer;
	}
			
	protected void serializeParams(Object[] params) throws IllegalArgumentException, IllegalStateException, IOException {
		if (params != null && params.length != 0)
		{
			// set method params
			serializer.startTag(null, XMLRPCTag.PARAMS);
			for (int i=0; i<params.length; i++) {
				serializer.startTag(null, XMLRPCTag.PARAM).startTag(null, XMLRPCISerializer.TAG_VALUE);
				iXMLRPCSerializer.serialize(serializer, params[i]);
				serializer.endTag(null, XMLRPCISerializer.TAG_VALUE).endTag(null, XMLRPCTag.PARAM);
			}
			serializer.endTag(null, XMLRPCTag.PARAMS);
		}
	}

}
