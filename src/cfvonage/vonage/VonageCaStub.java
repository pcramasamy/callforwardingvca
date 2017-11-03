package cfvonage.vonage;

import cfvonage.android.Preferences;
import android.util.Log;

public class VonageCaStub implements Vonage {

	public boolean configureCallForwarding(String number) {
		try {
			boolean b = login();
			Log.i("INFO", "starting activity1012r "+b);
			if (b) {
				return _configureCallForwarding(number, true);
			}
			return false;
		} catch(Exception e) {
			Log.e("ERROR", "Error during call forward", e);
			return false;
		}
	}

	public boolean clearCallForwarding() {
		try {
			return _configureCallForwarding(null, false);
		} catch(Exception e) {
			return false;
		}
	}

	public String getVonageNumber() {
		return Preferences.getVonageNumber();
	}
	
    private boolean login() throws Exception {
    	return stub();
    }
    
    private boolean _configureCallForwarding(String number, boolean configure) throws Exception {
    	return stub();
    }        
    
    private boolean logout() throws Exception {
    	return stub();
    }
    
    private boolean stub() {
    	try {
    		Thread.sleep(1000);
    	} catch(InterruptedException ie) {
    		ie.printStackTrace();
    	}
    	return true;    	
    }
}
