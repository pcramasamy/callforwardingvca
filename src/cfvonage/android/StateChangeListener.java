package cfvonage.android;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import cfvonage.vonage.Vonage;

public class StateChangeListener extends PhoneStateListener {
	
	static final String strUriCalls="content://call_log/calls"; 
	static final Uri callsUri = Uri.parse(strUriCalls);
	
	Context context;
	Vonage vonage;
	
	
	public StateChangeListener(Context c, Vonage v) {
		context = c;
		vonage = v;
	}
	
	private static StateChangeListener listener;
	private static boolean offhook = false;

	 public void onCallStateChanged(int state, String incomingNumber){
         
		 Log.i("INFO", "stage change listener...."+(state)+", "+incomingNumber);
		 
		 if (state == 2) // off-hook 
		 {
			 offhook = true;
		 }
		 
		 if (state == 0) {

			if (offhook && Preferences.isCallForwardingSet()) {
				// unlisten phone state listener
				Log.i("INFO", "unlisten....");
				TelephonyManager mgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				mgr.listen(StateChangeListener.getListener(context, vonage), PhoneStateListener.LISTEN_NONE);
				
				// register call log change observer to edit call log
				CallLogChangeObserver observer = new CallLogChangeObserver(new Handler(), Preferences.getCallForwardingFor());
				context.getContentResolver().registerContentObserver(callsUri, true, observer);
				
				// clear forwarding set
				AsyncTask<Void, Void, Boolean> clearTask = new ClearForwardAsyncTask(vonage);
				clearTask.execute();
				
				offhook = false;
			}
		 }
	}
	 
	 public static final StateChangeListener getListener(Context context, Vonage v) {
		 if (listener == null) 
			 listener = new StateChangeListener(context, v);
		 return listener;
	 }
	 
	 class ClearForwardAsyncTask extends AsyncTask<Void, Void, Boolean> {
		 	
		Vonage v;
		public ClearForwardAsyncTask(Vonage v) {
			this.v = v;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				return v.clearCallForwarding();

			} catch(Exception e) {
				Log.e("ERROR", "Error during clear forward", e);
				return false;
			}
		}
			
		 protected void onPostExecute(Boolean cleared) {
			 if (cleared) {
				Preferences.setCallForwardingFor(null);
			 }
		 }		
	 }	 	 

	 
	 class CallLogChangeObserver extends ContentObserver { 

		 	Handler handler;
		 	String dialedNumber;
		    public CallLogChangeObserver(Handler handler, String dialed) { 

		        super(handler);
		        this.handler = handler;
		        this.dialedNumber = dialed;

		    }

		@Override public boolean deliverSelfNotifications() { 
		    return false; 
		    }

		@Override public void onChange(boolean arg0) { 
		    super.onChange(arg0);
		     context.getContentResolver().unregisterContentObserver(this);

		     Log.i("INFO", "Notification on call log change");
		     TelephonyUtil.changeCallLog(dialedNumber, vonage.getVonageNumber(), context);
		     

		}	 
	 }
}
