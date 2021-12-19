package com.akb.sig.mpchat;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private Socket svc;
	private Scanner svr;
	private PrintStream svp;

	private Scanner usr;

	public static void main(String[] args) throws Exception {
		new Client();
	}

	public Client () throws Exception{
		usr = new Scanner (System.in);
		String tmp = "";
		System.out.printf("Which Chat-Server would you like to connect to?\n$ ");
		svc = new Socket (InetAddress.getByName(usr.nextLine()), 65535);
		System.out.printf("Enter a username (Everyone in the chat can see this!)\n$ ");
		svp.println("reghttps://www.youtube.com/watch?v=dQw4w9WgXcQ "+usr.nextLine());


		svr = new Scanner (svc.getInputStream());
		svp = new PrintStream (svc.getOutputStream());

		//———————[Comment-ICE]–––––––\\     //—————————[ECI-tnemmoC]——————\\
		// mehr infos unter: https://www.youtube.com/watch?v=dQw4w9WgXcQ    \\
		//\\—————————————————————————————\\_//——————————————————————————————//\\
		while (true) {
			while (!usr.hasNextLine())
				while (svr.hasNextLine())
					System.out.println(svr.nextLine().replace("recv ", ""));

			while (tmp != ":qa" && tmp != "exit") {
				tmp = usr.nextLine();
				switch (tmp.toLowerCase().split(" ")[0]) {

					case "reg":
						continue;
	
					case "exit":
					case ":qa":
					case "quit":
						svp.println(":qa");
						return;

					default: 
						svp.println("send " + tmp);
						continue;
				}
			}
		}	
	}
}












