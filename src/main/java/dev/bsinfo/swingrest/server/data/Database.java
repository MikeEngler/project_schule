package dev.bsinfo.swingrest.server.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.bsinfo.swingrest.model.Ablesung;
import dev.bsinfo.swingrest.model.Kunde;
import jakarta.annotation.ManagedBean;

@ManagedBean
public class Database {
	HashMap<String, Ablesung> ablesungen = new HashMap<>();
	HashMap<String, Kunde> kunden = new HashMap<>();
	
	public HashMap<String, Ablesung> getAblesungen() {
		return ablesungen;
	}
	public HashMap<String, Kunde> getKunden() {
		return kunden;
	}
	
	
	
}
