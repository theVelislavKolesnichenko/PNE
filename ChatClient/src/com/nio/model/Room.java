/**
 * 
 */
package com.nio.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Kolesnichenko
 *
 */
@SuppressWarnings("serial")
public class Room implements Serializable {

	private int id;
	private List<User> users;
	private String name;
	
	public Room(int id, String name, List<User> users) {
		this.id = id;
		this.name = name;
		this.users = users;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
