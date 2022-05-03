package it.unibo.robotWeb2020;

public class RequestMessageOnSock {

	private int id;
	private String name;

	public RequestMessageOnSock() {
		
	}

	public RequestMessageOnSock(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
