package cfvonage.android;

import cfvonage.vonage.Vonage;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class TelephonyUtil {

	private static final int NUMBER_INDEX = 3;
	private static final int ID_INDEX = 12;
	
	private static final String strUriCalls="content://call_log/calls"; 
	public  static final Uri callsUri = Uri.parse(strUriCalls);
	
	public static void call(String number, Context context) {
		String url = "tel:"+number;
		Intent vonageCall = new Intent(Intent.ACTION_CALL);
		vonageCall.setData(Uri.parse(url));
		vonageCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(vonageCall);
	}
	
	public static String cleanupPrefix(String number) {
		if (number.startsWith("+") || number.startsWith("0")) {
			return number.substring(1);
		} else {
			return number;
		}
	}
	
	public static void changeCallLog(String dialedNumber, String vonageNumber, Context context) {

		Cursor c = context.getContentResolver().query(callsUri, null, null, null, null); 
		
		try {
			c.moveToLast();
		} catch(Exception ignore) {}
		

		if (c.isLast()) { 
			Log.i("INFO", "last number: "+c.getString(NUMBER_INDEX));
			if (c.getString(NUMBER_INDEX).equals(vonageNumber)) {
				ContentValues cv = new ContentValues();
				String id = c.getString(ID_INDEX);
				for(int i=1;i<c.getColumnCount(); i++) {
					String value = c.getString(i);
					if (i == NUMBER_INDEX) {
						value = dialedNumber;
						Log.i("INFO", "col: "+c.getColumnName(i)+":"+c.getString(i)+":"+value);
					}
					cv.put(c.getColumnName(i), value);
				}
				int updated = context.getContentResolver().update(callsUri, cv, "_id="+id, null);
				Log.i("INFO", "updated: "+updated);
			}
		} 
	}
	

}
