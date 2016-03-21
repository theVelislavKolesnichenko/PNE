package com.nio.interfaces;

import java.nio.channels.SocketChannel;

public interface ServerListener {
	
	/**
	 * Server started successfully
	 * @param address
	 * @param port
	 */
	void onStarted(String address, int port);
	
	
	/**
	 * Server connection failed
	 * @param message for failed reason
	 */
	void onConnectionFailed(String message);
	
	
	/**
	 * Client connected to server
	 * @param client
	 */
	void onClientConnected(SocketChannel client);
	
	
	/**
	 * Client disconnected from server
	 * @param client
	 */
	void onClientDisconnected(SocketChannel client);
	
	
	/**
	 * Server received client message
	 * @param message
	 */
	void onClientMessageReceived(SocketChannel client, String message);
}
