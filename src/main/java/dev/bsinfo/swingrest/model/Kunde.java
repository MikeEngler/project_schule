package dev.bsinfo.swingrest.model;

import java.util.UUID;

public class Kunde {
	String name;
	String vorname;
	UUID id;

	public Kunde() {
		// TODO Auto-generated constructor stub
	}

	public Kunde(String name, String vorname) {
		this();
		this.name = name;
		this.vorname = vorname;
		this.id = UUID.randomUUID();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

}
