package com.akb.sig.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class HTTPClient extends Thread {

	private Socket client; 
	private Scanner clientIn;
	private PrintStream printer;
	private boolean isKeepAlive;

	private static final String root = "../Beispiele/http";


	public HTTPClient (Socket client){
		try {
			this.setName("httpclient."+client.getInetAddress().toString().replace("/", "").replace(".", "_")+"."+client.hashCode());
			// ^ Bennent den Thread um (einfachere Fehlersuche)

			// setzt client auf
			this.client = client; 
			this.clientIn = new Scanner(this.client.getInputStream());
			this.printer = new PrintStream(this.client.getOutputStream(), false, "utf-8");

			this.start(); // seperiert den Thread vom Haupt-thread

			// solange im Request connection: keep-alive spezifiziert wird tue:
			do {
				try {
					// versuche die Anfrage zu lesen 
					GET();
				} catch (FileNotFoundException e) {
					// wenn die Datei nicht gefunden wurde sende 404
					send (404, e, "PNF");	
				} catch (Exception e) {
					// bei sonstigen fehlern sende 500 und beende 
					send (500, e, "Error");
				} 
			} while (isKeepAlive);

			return;
		}catch (Exception e) {
			return;
		}
	}

	public void GET () throws FileNotFoundException, IllegalArgumentException{

		// lese Anfrage
		String rq = clientIn.nextLine(), path, tmp = "";
		this.isKeepAlive = false;
		while (!tmp.contains("user-agent")) {
			isKeepAlive = isKeepAlive || tmp.contains("keep-alive");
			tmp = clientIn.nextLine().toLowerCase();
		}

		// sende 500 be falschem Anfragetyp
		if (!(rq.startsWith("GET")||rq.startsWith("HEAD"))) 
			throw new IllegalArgumentException("Method "+rq.split(" ")[0]+" not Implemented!");

		// pfadberechnung
		path = rq.split(" ")[1].substring (rq.split(" ")[1].indexOf("/")).replaceAll("\\s", "");
		if (path.matches("\\s*/\\s*")) path = "/index.html";
		if (! (path.matches(".*\\.[html|js|ico|css]\\s*^"))) path += ".html"; 

		// sende 404 wenn datei nicht existiert
		if (!new File(root+path).exists()) throw new FileNotFoundException("File ("+path+") does not Exist");

		// lese datei
		String read = "";
		Scanner file = new Scanner (new File (root+path));
		while (file.hasNextLine())
			read += file.nextLine() + "\n";
		file.close();

		// sende 200 OK antwort wenn alles richtig läuft
		send (200, null, "OK", read);
	}

	/**
		If status is bigger than 299, an Error Page Is sent;
		@param status The HTTP statuscode
		@param e the thrown Exception (only NotNull if status > 299)
		@param text (text[0] has to be short status explaination) and rest is HTML/JS/CSS data
	 */
	private void send (int status, Exception e, String...text) {

		String response = "HTTP/1.1 " + status + " " + text[0] + "\nHost: "+ HTTPServer.ownIP + "\n\n";

		if (status < 300) // wenn kein fehler 
			for (int i=1; i < text.length; i++)
				response += text[i];
		else 
			sendErr (status, (e == null)? new Exception ("NullException") : e, text);

		printer.print(response);
	}

	@SuppressWarnings("deprecation")
	private void sendErr(int status, Exception e, String[] text) {
		String response = "HTTP/1.1 " + status + " " + text[0] + "\nHost: "+ HTTPServer.ownIP + "\n\n";

		try {
			Scanner sc = new Scanner (new File (root+"/Error.html"));
			while (sc.hasNextLine())
				response += sc.nextLine() + "\n";

			sc.close();

			// print Error to tmp/tmp_<threadName>
			File f = new File (root + "/tmp/tmp_"+Thread.currentThread().getName());
			PrintStream tmp = null;
			Scanner sc1 = null;

			f.createNewFile();
			tmp = new PrintStream (f);
			sc1 = new Scanner (f);

			e.printStackTrace(tmp);
			tmp.close();

			// read StackTrace Into Memory;
			String StackTrace = "";

			while (sc1.hasNextLine()) 
				StackTrace += sc1.nextLine() + "<br>\n";

			sc1.close();

			response = response.replace("$STACKTRACE", StackTrace).replace("$STATUS", ""+status);
		} catch (Exception $e) {
			$e.printStackTrace();
		}

		// sende Fehler 
		// falls Error.html nicht gelesen werden konnte wird nur die 500 / 404 Antwort gesendet
		printer.printf(response);

		try {
			client.close();
			super.stop(e);
		} catch (IOException $e) {
			$e.printStackTrace();
		}
	}
}