package cfvonage.android;

import android.content.SharedPreferences;
import cfvonage.vonage.Vonage;
import cfvonage.vonage.VonageAccount;
import cfvonage.vonage.VonageCa;
import cfvonage.vonage.VonageCaStub;

public class Preferences {
	
	private static String callForwardingSetFor = null;
	
	public static final String PREF_NAME = getVonage().getClass().getName();
	private static final String PREF_KEY_VONAGE_NUMBER = "VonageNumber";
	private static final String PREF_KEY_VONAGE_PASSWORD = "VonagePassword";
	
	
	private static final String vonageNumber = "6474783391";
	private static final String vonagePwd = "funpoorna123";
	
	private static final String vonagePrefix = "011";
	private static final String forwardingNumberPrefix = "+91";
	
	public static final String secondsBeforeForwarding = "1";
	public static final int sleepAfterForwardSet = 1000;

	public static final String version = "0.3 - adds call log change";
	
	public static boolean isForwardingNumber(String number) {
		return number.startsWith(getForwardingNumberPrefix());
	}
	
	public static boolean isVonageNumber(String number) {
		return number.equals(vonageNumber);
	}
	
	public static String getVonageNumber() {
		return vonageNumber;
	}
	
	public static boolean isCallForwardingSet() {
		return (callForwardingSetFor != null);
	}
	
	public static void setCallForwardingFor(String dialedNumber) {
		callForwardingSetFor = dialedNumber;
	}
	
	public static String getCallForwardingFor() {
		return callForwardingSetFor;
	}
	
	public static String getVonagePrefix() {
		return vonagePrefix;
	}
	
	public static Vonage getVonage() {
		return new VonageCaStub();
	}
	
	public static String getVersion() {
		return version;
	}
	
	public static String getForwardingNumberPrefix() {
		return forwardingNumberPrefix;
	}
	
	public static String getVonagePassword() {
		return vonagePwd;
	}
	
	public static void storeAccount(SharedPreferences settings) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PREF_KEY_VONAGE_NUMBER, getVonageNumber());
		editor.putString(PREF_KEY_VONAGE_PASSWORD, getVonagePassword());
		editor.commit();
	}
	
	
	public static VonageAccount getVonageAccount(SharedPreferences settings) {
		VonageAccount acc = new VonageAccount(settings.getString(PREF_KEY_VONAGE_NUMBER, ""), 
				settings.getString(PREF_KEY_VONAGE_PASSWORD, ""));
		return acc;
	}	
}
