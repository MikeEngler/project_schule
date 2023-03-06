package dev.bsinfo.swingrest.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

import dev.bsinfo.swingrest.model.Ablesung;
import dev.bsinfo.swingrest.model.Kunde;
import dev.bsinfo.swingrest.server.data.Database;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Application;

public class Server extends Application {

	public static Database DATABASE = new Database();


	private static HttpServer server = null;

	public static void startServer(String url, boolean loadFromFile) {

		final String pack = "dev.bsinfo.swingrest.server.controller";
		if (server == null) {

			System.out.println("Start server");
			System.out.println(url);

			final ResourceConfig rc = new ResourceConfig().packages(pack);

			server = JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
			System.out.println("Ready for Requests....");

			if (loadFromFile) {
				// Daten aus der Datenbank laden
				File file = new File("database_abl.csv");
				if (file.exists()) {
					try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr);) {
						for (String line : br.lines().toArray(String[]::new)) {
							String[] entry = line.split(";");
							Ablesung ablesung = new Ablesung(entry[0], // Zählernummer
									LocalDate.parse(entry[1]), // datum
									null, // Kunde
									entry[3], // Kommentar
									Boolean.parseBoolean(entry[4]), // neuEingebaut
									Integer.parseInt(entry[5]) // Zahlerstand
							);
							DATABASE.getAblesungen().put(ablesung.getId().toString(), ablesung);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	public static void stopServer(boolean saveToFile) {

		File file = new File("database_abl.csv");

			try (FileWriter fw = new FileWriter(file); BufferedWriter bw = new BufferedWriter(fw)) {

				for (Ablesung ablesung : DATABASE.getAblesungen().values()) {
					fw.append(ablesung+"\r\n");
					System.out.println(ablesung.toString());
				}
			} catch (IOException e) {
			}
		System.exit(0);
	}

}
