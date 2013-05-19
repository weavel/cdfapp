package cafe.feestneus;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class SynchData
{
	public static void postData(String url, List<NameValuePair> nameValuePairs) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);

	    try
	    {
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        //HttpResponse response =
	        httpclient.execute(httppost);
	    }
	    catch (ClientProtocolException e)
	    {
	        // TODO Auto-generated catch block
	    }
	    catch (IOException e)
	    {
	        // TODO Auto-generated catch block
	    }
	} 
}
