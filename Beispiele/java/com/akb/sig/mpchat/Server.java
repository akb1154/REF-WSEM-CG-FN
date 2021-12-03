import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Server {

	private static final int PORT = 38174;
	private static ServerSocket socket;
	
	private static HashMap<String, Server.User> users;
	
	public Server (String IP) throws IOException {
		// auslagern der Registrierung auf Worker-Thread
		socket = new ServerSocket(PORT, 30, InetAddress.getByName(IP)) {
			@Override
			public String toString () {
				String randname;
				while (users.size() < 30) {
					try {
						randname = getRandomName();
						users.put(randname, new User(this, randname, socket.accept()));
					}catch(Exception e) {
						continue;
					}
				}
				
				return super.toString();
			}
		};
		
		// nimmt anfragen an
		new Thread() {
			@SuppressWarnings("resource")
			@Override
			public void run () {
				try {
					socket.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
 		}.start();
		
 		
	}

	protected String getRandomName() {
		return "temp-"+new java.util.Random().hashCode();
	}

	private void onMessageRecieved (String user, String msg) {
		
	}
	
	public static class User extends Thread {

		private String uname;
		private Socket user;
		private PrintStream uprint;
		private Scanner uin;
		private Server serv;
		
		public User(Server serv, String randname, Socket user) throws IOException {
			this.user = user;
			this.serv = serv;
			this.uprint = new PrintStream(this.user.getOutputStream());
			this.uin = new Scanner(this.user.getInputStream());
			
			// get username
			String request = uin.nextLine();
			if (request.startsWith("register")) {
				uname = request.split(" ")[1];
				if (uname != "System" && uname != "") 
					uprint.println("200 OK");
				else
					uprint.println("401 IllegalName");
			} else {
				uprint.println("400 BadRequest");
			}
			
			// rename in list
			users.remove(randname);
			users.put(uname, this);
			
			// split into new Thread
			super.start();
		}

		public void run () {
			String userIn = "";
			while (true) {
				userIn = uin.nextLine();
				if (userIn.matches("$:qa|quit|exit^"))
					try {
						quit();
					} catch (IOException e) {
						e.printStackTrace();
					}
				else if (userIn.startsWith("print") || (userIn.contains("%20") && !userIn.startsWith("register")))
					serv.onMessageRecieved(uname, userIn.replace("print ", "").replace("print", ""));
				else
					uprint.println("400 BadRequest");
				
				while (!uin.hasNextLine())
					try {
						sleep(1);
					}catch(Exception e) {
						continue;
					}
			}
		}
		
		
		public void println (String user, String msg) {
			boolean isSysMsg = (user == "System");
			// System Message Format: [System]: $msg
			// User   Message Format: <$user> $msg
			uprint.printf("%s%s%s %s\n", (isSysMsg? "[" : "<"), user, (isSysMsg? "]:" : ">"), msg);
		}
		
		public void quit() throws IOException {
			users.remove(uname);
			uin.close();
			uprint.println("200 DISC");
			serv.onSystemMessage ("user "+uname+" left");
			user.close();
		}		
	}

	public void onSystemMessage(String msg) {
		onMessageRecieved("System", msg);
	}

}
