package com.akb.sig.mpnim;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {

	public ServerSocket server;
	public Socket player0, player1;
	public PrintStream p0, p1;
	public Scanner p0s, p1s;
	
	private static int Start;

	private int Marbles;
	private boolean isRematch;

	public static void main (String[] args) throws Exception {
		if (args[0] == "-?" || args[0] == "--help") {
			System.out.println(" Syntax: java -jar mpnim.server.jar <Bind IP> [Starting Marbles] ");
			System.exit(0);
		}
		Start = (args.length == 2)? Integer.parseInt(args[1]) : 64;
		new Server (args[0]);
	}

	public Server (String IP) throws Exception {
		server = new ServerSocket (65015, 2, InetAddress.getByName(IP));
		player0 = server.accept();
		player1 = server.accept();
		System.err.printf("Now Playing: user@%s vs user@%s\n\n", player0.getInetAddress().toString(), player1.getInetAddress().toString());
		p0 		= new PrintStream (player0.getOutputStream());
		p1 		= new PrintStream (player1.getOutputStream());
		p0s 	= new Scanner 	  ( player0.getInputStream());
		p1s		= new Scanner 	  ( player1.getInputStream());
		isRematch = true;
		while (isRematch) game();
	}
	
	public void game () {
		Marbles = Start;
		int winner = -0xff;
		isRematch = false;
		
		while (winner == -0xff) {
			p0.printf("How many marbles would you like to take? [1~%1d, %2d left]\\n$ \n", getMarbleMax(), Marbles);
			Marbles -= p0s.nextInt();
			if (Marbles == 0) {
				winner = 0;
				break;
			}
			p1.printf("How many marbles would you like to take? [1~%1d, %2d left]\\n$ \n", getMarbleMax(), Marbles);
			Marbles -= p1s.nextInt();
			if (Marbles == 0) {
				winner = 1;
				break;
			}			
		}
		
		p0.printf("%s\\nWould you like to have a rematch? [y/n]\\n$ \n", (winner == 0)? "You Won!" : "You Lost!");
		p1.printf("%s\\nWould you like to have a rematch? [y/n]\\n$ \n", (winner != 0)? "You Won!" : "You Lost!");
	
		isRematch = (p0s.nextLine().toLowerCase().matches("y") && p1s.nextLine().toLowerCase().matches("y")); 
		return;
	}

	private int getMarbleMax() {
		return (Marbles > 3)? 3 : Marbles;
	}

	

}