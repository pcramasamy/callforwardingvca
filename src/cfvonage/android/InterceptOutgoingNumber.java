package cfvonage.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import cfvonage.vonage.Vonage;
import cfvonage.vonage.VonageAccount;

public class InterceptOutgoingNumber extends BroadcastReceiver {
	 

	 public void onReceive(Context context, Intent intent){

		 String dialedNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		 if (Preferences.isForwardingNumber(dialedNumber)) {
			 String number = TelephonyUtil.cleanupPrefix(dialedNumber);
			 Vonage v = Preferences.getVonage();
			 
             Toast.makeText(context, "Setting Call Forward to Vonage.ca"
             		, Toast.LENGTH_LONG).show();
             
             SharedPreferences settings = context.getSharedPreferences(Preferences.PREF_NAME, Context.MODE_PRIVATE);
             Preferences.storeAccount(settings);
             
             AsyncTask<String, Void, String> at = new ForwardAsyncTask(context, v, dialedNumber);
             at.execute(new String[] { number });
             
			 Log.i("INFO", "onReceive return null");

             setResultData(null);
         } 
	 }
	 
	 class ForwardAsyncTask extends AsyncTask<String, Void, String> {
		 	
		Context context;
		Vonage v;
		String dialedNumber;
		
		public ForwardAsyncTask(Context context, Vonage v, String dialed) {
			this.context = context;
			this.v = v;
			dialedNumber = dialed;
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
				boolean res = v.configureCallForwarding(params[0]);
				
				if (res) {
					return v.getVonageNumber();
				} else {
					return null;
				}				
			} catch(Exception e) {
				Log.e("ERROR", "errrrrr", e);
				return null;
			}
		}
			
		 protected void onPostExecute(String vonageNumber) {
			if (vonageNumber != null) {
				try {
					Thread.sleep(Preferences.sleepAfterForwardSet);
				} catch(Exception e) {
					
				}
				// make vonage call
				Toast.makeText(context, "Making the vonage call", Toast.LENGTH_SHORT).show();
				TelephonyUtil.call(vonageNumber, context);

				// mark forwarding has been set
				Preferences.setCallForwardingFor(dialedNumber);
				
				// register listener to identify when the call is ended
	 			TelephonyManager mgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	 			mgr.listen(StateChangeListener.getListener(context, v), PhoneStateListener.LISTEN_CALL_STATE);
				Log.i("INFO", "registered listener");
				
			} else {
				Toast.makeText(context, "Cannot configure call forwarding with vonage.ca", Toast.LENGTH_SHORT).show();
			}

            SharedPreferences settings = context.getSharedPreferences(Preferences.PREF_NAME, Context.MODE_PRIVATE);
            VonageAccount acc = Preferences.getVonageAccount(settings);
			Log.i("INFO", "stored pwd "+acc.getPassword());
			
		 }		
	 }	 
	 
}
