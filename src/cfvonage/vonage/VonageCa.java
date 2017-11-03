package cfvonage.vonage;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import cfvonage.android.Preferences;

import android.util.Log;

public class VonageCa implements Vonage {

	DefaultHttpClient httpclient = new DefaultHttpClient(); 
	static final Vonage v = new VonageCa();
	
	private static final String did = "1" + Preferences.getVonageNumber();
	
	// Value to validate the http response
	private static final String validate = "Dashboard";
	
	public boolean configureCallForwarding(String number) {
		try {
			boolean b = login();
			if (b) {
				Log.i("INFO", "Login: Logged in");
				return _configureCallForwarding(checkPrefix(number), true);
			}
			return false;
		} catch(Exception e) {
			Log.e("ERROR", "Error during call forward", e);
			return false;
		}
	}

	public boolean clearCallForwarding() {
		try {
			boolean b = login();
			if (b) {
		    	HttpPost httpost = new HttpPost("https://secure.vonage.ca/webaccount/features/callforwarding/edit.htm");
		    	List <NameValuePair> nvps = new ArrayList <NameValuePair>(); 
		    	nvps.add(new BasicNameValuePair("phoneNumber", did)); 
		    	nvps.add(new BasicNameValuePair("did", did)); 
		    	nvps.add(new BasicNameValuePair("enableCallForwarding", "false")); 
		    	
		    	httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); 
		    	
		    	HttpResponse response = httpclient.execute(httpost);
		    	
		    	BasicResponseHandler br = new BasicResponseHandler();
		    	String responseHtml = br.handleResponse(response);
		    	
		    	return (responseHtml.indexOf(validate) != -1);
			} else {
				return false;
			}
		} catch(Exception e) {
			Log.e("ERROR", "Error during clear forward", e);
			return false;
		}
    }

	public String getVonageNumber() {
		return Preferences.getVonageNumber();
	}
	
    private boolean login() throws Exception {
    	if (isAlreadyLoggedIn()) {
			Log.i("INFO", "Login: Already logged in");
   		return true;
    	} else {
	    	HttpPost httpost = new HttpPost("https://secure.vonage.ca/webaccount/public/login.htm"); 
	    	List <NameValuePair> nvps = new ArrayList <NameValuePair>(); 
	    	nvps.add(new BasicNameValuePair("username", Preferences.getVonageNumber())); 
	    	nvps.add(new BasicNameValuePair("password", Preferences.getVonagePassword())); 
	    	httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); 
	    	HttpResponse response = httpclient.execute(httpost);
	    	
	    	BasicResponseHandler br = new BasicResponseHandler();
	    	String responseHtml = br.handleResponse(response);
	    	
	    	return (responseHtml.indexOf(validate) != -1);
    	}
    }
    
    private boolean isAlreadyLoggedIn() throws Exception {
    	HttpPost httpost = new HttpPost("https://secure.vonage.ca/webaccount/dashboard/index.htm"); 
    	HttpResponse response = httpclient.execute(httpost);
    	
    	BasicResponseHandler br = new BasicResponseHandler();
    	String responseHtml = br.handleResponse(response);
    	
    	return (responseHtml.indexOf(validate) != -1);
    }
    
    private boolean _configureCallForwarding(String number, boolean configure) throws Exception {
    	HttpPost httpost = new HttpPost("https://secure.vonage.ca/webaccount/features/callforwarding/edit.htm");
    	List <NameValuePair> nvps = new ArrayList <NameValuePair>(); 
    	nvps.add(new BasicNameValuePair("phoneNumber", did)); 
    	nvps.add(new BasicNameValuePair("did", did)); 
    	nvps.add(new BasicNameValuePair("action", "save")); 
    	nvps.add(new BasicNameValuePair("enableCallForwarding", String.valueOf(configure))); 
    	nvps.add(new BasicNameValuePair("simulRingEnabled", "false")); 
    	nvps.add(new BasicNameValuePair("currTab", "CF")); 
    	nvps.add(new BasicNameValuePair("singleAddress", number)); 
    	nvps.add(new BasicNameValuePair("callForwardingSeconds", Preferences.secondsBeforeForwarding)); 
    	
    	httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8)); 
    	
    	HttpResponse response = httpclient.execute(httpost);
    	
    	BasicResponseHandler br = new BasicResponseHandler();
    	String responseHtml = br.handleResponse(response);
    	
    	return (responseHtml.indexOf(validate) != -1);
    }        
    
    private boolean logout() throws Exception {
    	HttpPost httpost = new HttpPost("https://secure.vonage.ca/webaccount/public/logoff.htm"); 
    	HttpResponse response = httpclient.execute(httpost);
    	
    	return true;
    }
    
    private String checkPrefix(String number) {
    	if (number.startsWith(Preferences.getVonagePrefix())) {
    		return number;
    	} else {
    		return Preferences.getVonagePrefix()+number;
    	}
    }

	public static Vonage getVonage() {
		return v;
	}      	

}
