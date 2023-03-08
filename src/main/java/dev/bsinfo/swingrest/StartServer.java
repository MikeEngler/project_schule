package dev.bsinfo.swingrest;

import dev.bsinfo.swingrest.server.Server;

public class StartServer {

	public static void main(final String[] args) throws InterruptedException {
		Server.startServer("http://localhost:8080/test", true);
		Thread.sleep(100000);
		Server.stopServer(true);
	}
}