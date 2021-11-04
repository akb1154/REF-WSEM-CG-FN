package com.akb.sig.mpnim;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class MPNIM_Client extends Socket implements Runnable {

	private Scanner server, user;
	private PrintStream pStream; 

	public MPNIM_Client(String IP) throws IOException {
		super(InetAddress.getByName(IP), 14913);
		for(int cntr=0; !super.isConnected(); cntr++)
			if (cntr >= 5) {
				System.err.printf("[ERROR]: Failed to connect to %s:14913! please confirm that: \nMPNIM_Server is running on the Device you are trying to connect to and that \nPort 14913 (tcp) is allowed on your firewall and Router!", IP);
				System.exit(-1);
			}
			else 
				super.connect(new InetSocketAddress(InetAddress.getByName(IP), 14913));

		System.out.printf("Connected to %s:14913", IP);
		if (Thread.currentThread().getName().toLowerCase() == "server")
			new Thread (this, "player").start();

	}

	@Override
	public void run() {
		try {
			server = new Scanner (super.getInputStream());
			pStream = new PrintStream(super.getOutputStream());
			user = new Scanner(System.in);	
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-293);
		}
		
		String readin, reply;
		while (super.isConnected()){
			readin = server.nextLine().replace("/n", "\n");
			System.out.print(readin);
			if (readin.contains ("$ ")) {
				reply = user.nextLine();
				pStream.println(reply);
			}
			else continue;
		}
	}
}
