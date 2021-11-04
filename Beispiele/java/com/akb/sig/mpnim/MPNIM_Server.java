package com.akb.sig.mpnim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MPNIM_Server {

	public static final short GAME_PORT = 14913;
	private static String IP;
	
	private ServerSocket socket;
	private Socket player0, player1;
	private BufferedReader pl0r, pl1r;
	private PrintStream pl0p, pl1p;
	
	public MPNIM_Server (boolean isPlayer0onServerThread) throws IOException {
		if (isPlayer0onServerThread)
			Thread.currentThread().setName("server");		
		
		socket = new ServerSocket (GAME_PORT, 2, InetAddress.getByName(IP));
		player0 = (isPlayer0onServerThread)? new MPNIM_Client (IP) : socket.accept();
		player1 = socket.accept();
		pl0r = new BufferedReader ( new java.io.InputStreamReader(player0.getInputStream(), "utf-8"));
		pl1r = new BufferedReader ( new java.io.InputStreamReader(player1.getInputStream(), "utf-8"));
		pl0p = new PrintStream ( player0.getOutputStream());
		pl1p = new PrintStream ( player1.getOutputStream());
		
		// GameLoop 
		boolean isRM = false;
		do {
			isRM = this.gameLoop();			
		} while (isRM);
		
		// close Connections
		pl0p.close(); pl1p.close();
		pl0r.close(); pl1r.close();
		player0.close();
		player1.close();
	}
	
	/** 
	 * @return true if the Players select to rematch else false;
	 */
	private boolean gameLoop() throws IOException {
		int Marbles = Integer.MAX_VALUE;
		pl0p.printf("/n/nHow many marbles would you like to play with?/n$ \n");
		
		try {
			Marbles = Integer.parseInt(pl0r.readLine());
		} catch (Exception e) {
			Marbles = new java.util.Random().nextInt(128) + 1; // range: 1~128;
		}		

		int winner = 0x7f;
		while (Marbles > 0 && winner != 0x7f) {
			pl0p.printf("How many Marbles (%2d left) would you like to take? /n$ \n", Marbles);
			Marbles -= Integer.parseInt(pl0r.readLine());
			if (Marbles <= 0) winner = 0;

			pl1p.printf("How many Marbles (%2d left) would you like to take? /n$ \n", Marbles);
			Marbles -= Integer.parseInt(pl1r.readLine());
			if (Marbles <= 0) winner = 1;
		}

		pl0p.printf("You %s! /nWould you like a rematch? [y/n]/n$ \n", (winner == 0)? "Won" : "lost");
		pl1p.printf("You %s! /nWould you like a rematch? [y/n]/n$ \n", (winner == 1)? "Won" : "lost");

		String pl0reply = pl0r.readLine(), pl1reply = pl1r.readLine();
		pl0reply = pl0reply.toLowerCase().trim().charAt(0)+"";
		pl1reply = pl1reply.toLowerCase().trim().charAt(0)+"";

		return (pl0reply == "y" || pl0reply == "z") && (pl1reply == "y" || pl1reply == "z");
	}

	public static void main(String[] args) throws IOException {
		boolean tmp = false;
		try {
			IP = (args[0].matches("...\\.") || args[0].matches(".*:.*:") || args[0].matches(".*\\..*\\..*"))? args[0] : "127.0.0.1";
			if (args[0] == "true" || args[0] == "false") tmp = Boolean.parseBoolean(args[0]);
			else if (args[1] == "true" || args[1] == "false") tmp = Boolean.parseBoolean(args[1]);
			else tmp = false;
		} catch (ArrayIndexOutOfBoundsException AIOBE) {}
	
		new MPNIM_Server (tmp);
	}
	
}
