/**
 * 
 */
package com.nio.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.List;

import com.nio.model.*;
import com.nio.util.Constants;

/**
 * @author Kolesnichenko
 *
 */
public class ChannelHandler {

    public static void sendMsg(SocketChannel socketChannel, Message msg) throws IOException {
        //msg += "\n";
        System.out.println("Sending msg : " + msg);
        ByteBuffer byteBuffer = ByteBuffer.wrap(Serialization.serialize(msg));
        socketChannel.write(byteBuffer);
    }
    
    public static Message readMsg(SocketChannel socketChannel) throws IOException, ClassNotFoundException {
    	ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();

         byte[] headerBytes = new byte[byteBuffer.remaining()];
         byteBuffer.get(headerBytes);
         Message msg = null;
         msg = Serialization.deserialize(headerBytes);
         return msg;
    }
	
    //ToDo: change User with Room
    public static void sendRooms(SocketChannel socketChannel, List<Room> rooms) throws IOException {
        System.out.println("Sending msg : " + rooms);
        ByteBuffer byteBuffer = ByteBuffer.wrap(Serialization.serialize(rooms));
        socketChannel.write(byteBuffer);
    }
    
  //ToDo: change User with Room
    public static List<Room> readRooms(SocketChannel socketChannel) throws IOException, ClassNotFoundException {
    	ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();

         byte[] headerBytes = new byte[byteBuffer.remaining()];
         byteBuffer.get(headerBytes);
         List<Room> rooms = null;
         rooms = Serialization.deserialize(headerBytes);
         return rooms;
    }
    
	/**
	 * Client send file
	 * @return send status
	 */
	public static boolean sendFile(){
		
		return false;
		
	}
	
	/**
	 * Server transfer file send from Client 1
	 * to save from Client 2
	 * @return transfer status
	 */
	public static boolean transferFile(){
		
		return false;
	}

	/**
	 * Client save file
	 * @return save status
	 */
	public static boolean saveFile(){
		
		return false;
		
	}
}