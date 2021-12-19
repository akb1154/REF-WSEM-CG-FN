package com.akb.sig.mpchat;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

	private static Server server;

	private ServerSocket $server;
	private HashMap<String, Server.Client> users;

	private HashMap<Integer, String> msgList;
	private int MessageIndex;

	/**
	 * @param IP the IP address (in String form) to bind to
	 */
	public Server (String IP) throws Exception {
		server 	= this;
		$server = new ServerSocket (65535, 15, InetAddress.getByName(IP));
		msgList = new HashMap<Integer, String>();
		users	= new HashMap<String, Server.Client>();
		MessageIndex = 0;

		while (users.size() < 15) {
			new Client ($server.accept());
		}
	}

	public static void register (String uname, Server.Client usr) {
		synchronized (server.users) {
			server.users.put(uname, usr);
		}
	}
	
	public static String getMessage (int index) {
		synchronized (server.msgList) {
			return server.msgList.get(index);
		}
	}

	public static int getMessageIndex() {
		synchronized (server.msgList) {
			// only one thread at a time
			return server.MessageIndex = server.msgList.size()-1;
		}
	}

	public static void addMessage (String usr, String txt) {
		synchronized (server.msgList) {
			server.msgList.put(server.msgList.size(), "["+usr+"]: "+txt);
		}
	}

	public static class Client extends Thread {

		private Socket user;
		private Scanner usr;
		private PrintStream usp;

		private int lastReadIndex;

		public Client(Socket user) throws Exception {
			this.user = user;
			this.usr = new Scanner (this.user.getInputStream());
			this.usp = new PrintStream(this.user.getOutputStream());

			// registriert den Benutzer unter angegebenen Benutzernamen
			String read = usr.nextLine(); 
			while (!read.startsWith("reg")) {
				System.err.println("[Thread-"+super.getId()+"]: first message has to be reg");
				usp.println("please register first! \\n enter: reg <username>");
				read = usr.nextLine();
			}

			Server.register(read.split(" ")[0], this);
			super.setName(read.split(" ")[0]);
			
			Server.addMessage("server", "<usrinfo> User "+getName()+" (Thread-Id"+getId()+") joined");
			System.err.printf("<usrinfo> User %s (Thread-ID %d) joined", getName(), getId());
			// entkuppelt Thread von Main
			this.start();
		}

		public boolean hasNext () {
			return usr.hasNextLine();
		}

		private void updateLocalIndex () {
			int mic = getMessageIndex();
			if (this.lastReadIndex == mic) return;

			// send all msg betweeen lastRead and most Current message
			for (;lastReadIndex < mic+1; lastReadIndex++)
				usp.println("recv " + Server.getMessage(lastReadIndex));
			return;
		}

		private void sleep () {
			try {
				Client.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run () {
			String last_in = "";
			while (true) {

				updateLocalIndex();
				while (!usr.hasNextLine()) sleep ();

				last_in = usr.nextLine();
				if (last_in == ":qa" | last_in == "exit")
					break;
				else
					Server.addMessage(getName(), last_in.replace("send ", " "));
			}

			Server.addMessage("server", "<usrinfo> User "+getName()+" (Thread-Id"+getId()+") left");
			System.err.printf("<usrinfo> User %s (Thread-ID %d) left", getName(), getId());
			
			try {
				this.join();
			} catch (InterruptedException e) {
				e.printStackTrace();

			}
		}
	}
}