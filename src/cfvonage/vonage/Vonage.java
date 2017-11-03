package cfvonage.vonage;

public interface Vonage {
	public boolean configureCallForwarding(String number);
	public boolean clearCallForwarding();
	
	public String getVonageNumber();

}
