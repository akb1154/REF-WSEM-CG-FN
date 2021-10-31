package com.akb.sig.mpnim;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;

public class MPNIM_Server {
	
	public static final int GAME_PORT = 14913; 
	
	private ServerSocket serverSocket;
	private Socket player0, player1;
	private PrintStream p0ps, p1ps;
	private BufferedReader p0br, p1br;
	private int Marbles;


	public MPNIM_Server (String IP, boolean player0onServerThread) throws Exception {
		serverSocket = new ServerSocket (GAME_PORT, 2, InetAddress.getByName(IP));
		this.Marbles = 0b01000000; // = 0x40 && 0d??
		this.player0 = init_pl0(player0onServerThread, IP);
		this.player1 = serverSocket.accept();
		this.p0ps = new PrintStream (player0.getOutputStream());
		this.p1ps = new PrintStream (player1.getOutputStream());
		this.p0br = new BufferedReader (new java.io.InputStreamReader (player0.getInputStream()));
		this.p1br = new BufferedReader (new java.io.InputStreamReader (player1.getInputStream()));

		p0ps.println ("you are player 0");
		p1ps.println ("you are player 1");

		//main Game loop
		while (Marbles > 0){
			Marbles = executeTurn(0);
			if (Marbles <= 0) win(0);
			Marbles = executeTurn(1);
			if (Marbles <= 0) win(1);
		}
	}

	private int executeTurn (int player) throws IOException {
		String readIn;

		if (player == 0) {
			p0ps.println ("current Marbles: "+Marbles);
			readIn = p0br.readLine();
		}
		else {
			p1ps.println ("current Marbles: "+Marbles);
			readIn = p1br.readLine();
		}

		Marbles -= Integer.parseInt(readIn) % 4;
		return Marbles;
	}

	private void win (int player) throws Exception {
		if (player == 0) {
			p0ps.println("Player \u3007 won!");
			player0.close();
			p1ps.println("Player \u3007 won!");
			player1.close();
			serverSocket.close();
			System.exit(0);
		}
		else {
			p0ps.println("Player \u3192 won!");
			player0.close();
			p1ps.println("Player \u3192 won!");
			player1.close();
			serverSocket.close();
			System.exit(1);
		}
	}

	private MPNIM_Client init_pl0 (boolean isOnServerThread, String IP) throws IOException {
		if (!isOnServerThread)
			player0 = serverSocket.accept();
		else 
			player0 = new MPNIM_Client(IP);
		
		return player0;
	}


	public static void main (String[] args) throws Exception{
		if (args.length == 0) throw new Exception ("Syntax: java MPNIM_Server <IP_address> [is Player0 on Server Thread];");

		if (args.length == 2) 
			new MPNIM_Server (args[0], Boolean.parseBoolean(args[1]));
		else
			new MPNIM_Server (args[0], false);
	}

}
