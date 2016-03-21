package com.nio.model;

import java.io.Serializable;

import com.nio.util.MessageType;

@SuppressWarnings("serial")
public class Message implements Serializable {
	
	private MessageType type;
	private String text;
	private int roomID;
	private int senderID;
	
	public Message(MessageType type, int senderID, String text, int roomID) {
		this.type = type;
		this.senderID = senderID;
		this.text = text;
		this.roomID = roomID;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getRoomID() {
		return roomID;
	}
	
	public void setRoomID(int recieverID) {
		this.roomID = recieverID;
	}
	
	public int getSenderID() {
		return senderID;
	}
	
	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
	public MessageType getMessageType() {
		return this.type;
	}
	
	public void setMessageType(MessageType type) {
		this.type = type;
	}
	
    @Override
    public String toString() {
        return type + text + roomID + senderID;
    }
}
