package Message;



public enum MessageType {
	
	Login,
	Logout,
	ChangePassword,
	CreateLogin,
	
	CreateToDo,
	DeleteToDo,
	
	Result,
	Ping,
	
	
	Error,
	Hello;
	
    public static MessageType parseType(String typeName) {
    	MessageType type = MessageType.Error;
    	for (MessageType value : MessageType.values()) {
    		if (value.toString().equals(typeName)) type = value;
    	}
    	return type;
    }
	
    /**
     * Determine the message type from the actual class of this object
     */
    public static MessageType getType(Message msg) {
    	MessageType type = MessageType.Error;
    	if (msg instanceof Message_Ping) type = Ping;
    	else if (msg instanceof Message_Login) type = Login;
    	else if (msg instanceof Message_Logout) type = Logout;
    	else if (msg instanceof Message_ChangePassword) type = ChangePassword;
    	else if (msg instanceof Message_CreateLogin) type = CreateLogin;
//    	else if (msg instanceof Message_CreateToDo) type = CreateToDo;
//    	else if (msg instanceof Message_DeleteToDo) type = DeleteToDo;


//    	else if (msg instanceof Message_Hello) type = Hello;

    	return type;
    }	
	

}
