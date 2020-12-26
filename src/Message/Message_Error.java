package Message;

import server.Client;

public class Message_Error extends Message {

	public Message_Error() {
		super(new String[] {"MessageError", "Invalid command"});		
	}
	
	/**
	 * This message type does no processing at all
	 */
	@Override
	public void process(Client client) {
	}
}
