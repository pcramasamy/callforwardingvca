package cfvonage.android;

import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

public class CallForwardMainActivity extends Activity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        String msg = "<body bgcolor='#FFFFFF'><br>";
        msg = msg + "secondsBeforeForwarding: "+Preferences.secondsBeforeForwarding+"<br>";
        msg = msg + "sleepAfterForwardSet: "+Preferences.sleepAfterForwardSet+"<br><br><br>";
        msg = msg + "Version: "+Preferences.getVersion();
        msg = msg + "</body>";
    	
        String responseHtml = msg;
        try {
        	long start = System.currentTimeMillis();
			long end = System.currentTimeMillis();

        } catch(Exception e) {
        	Log.e("HelloAndroid", "error", e);
        }
        tv.setText(msg);
        
        WebView wv = new WebView(this);
        wv.loadData(responseHtml, "text/html", "utf-8");

//        setContentView(wv);
        setContentView(R.layout.main);
    }
}