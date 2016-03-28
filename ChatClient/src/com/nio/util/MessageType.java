/**
 * 
 */
package com.nio.util;

/**
 * @author Kolesnichenko
 */
public enum MessageType {

	SEND_MESSAGE("SendMessage"),
	FILE_UPLOAD("FileUpload"),
	FILE_DOWNLOAD("FileDownload"),
	LOG_IN("LogIn"),
	DISCONNECT("Disconnect");
	
    private String command;

	MessageType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
    
    public static boolean isCommand(String str) {
        for (MessageType command : MessageType.values()) {
            if (str.contains(command.getCommand())) {
                return true;
            }
        }
        return false;
    }
}
