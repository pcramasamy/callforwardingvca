package cfvonage.android;

public class PreferenceException extends Exception {
	
	public PreferenceException(String prefKey, String message, Throwable t) {
		super(prefKey+": "+message, t);
	}

}
