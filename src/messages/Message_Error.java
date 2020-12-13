package messages;

import java.util.ArrayList;

public class Message_Error extends Message {

	private static final String ELEMENT_INFO = "info";
	
	private String info;
	
    public Message_Error() {
    	super();
    }

	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		this.info = findAttribute(pairs, ELEMENT_INFO);
	}    
	
	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
		pairs.add(new NameValue(ELEMENT_INFO, this.info));
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}	
}
