package com.example.domain;

public class ParticipateEvent {

	private int id;

	private String username;

	private String participate;

	public ParticipateEvent() {}

	public ParticipateEvent(String participate) {
		this.participate = participate;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getParticipate() {
		return participate;
	}

	public void setParticipate(String participate) {
		this.participate = participate;
	}

}
