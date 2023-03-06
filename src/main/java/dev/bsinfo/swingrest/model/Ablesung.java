package dev.bsinfo.swingrest.model;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ablesung {
	UUID id;
	String zaehlernummer;
	LocalDate datum;
	Kunde kunde;
	String kommentar;
	boolean neuEingebaut;
	@JsonProperty(required = true)
	Number zaehlerstand;

	public Ablesung() {

		this.id = UUID.randomUUID();
	}

	public Ablesung(String zaehlernummer, LocalDate datum, Kunde kunde, String kommentar, boolean neuEingebaut,
			Number zaehlerstand) {
		this();
		this.zaehlernummer = zaehlernummer;
		this.datum = datum;
		this.kunde = kunde;
		this.kommentar = kommentar;
		this.neuEingebaut = neuEingebaut;
		this.zaehlerstand = zaehlerstand;
	}

	public String getZaehlernummer() {
		return zaehlernummer;
	}

	public void setZaehlernummer(String zaehlernummer) {
		this.zaehlernummer = zaehlernummer;
	}

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	public Kunde getKunde() {
		return kunde;
	}

	public void setKunde(Kunde kunde) {
		this.kunde = kunde;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public boolean isNeuEingebaut() {
		return neuEingebaut;
	}

	public void setNeuEingebaut(boolean neuEingebaut) {
		this.neuEingebaut = neuEingebaut;
	}

	public Number getZaehlerstand() {
		return zaehlerstand;
	}

	public void setZaehlerstand(Number zaehlerstand) {
		this.zaehlerstand = zaehlerstand;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(zaehlernummer);
		builder.append(";");
		builder.append(datum);
		builder.append(";");
		builder.append(kunde);
		builder.append(";");
		builder.append(kommentar);
		builder.append(";");
		builder.append(neuEingebaut);
		builder.append(";");
		builder.append(zaehlerstand);
		builder.append(";");
		return builder.toString();
	}
	
	

}
