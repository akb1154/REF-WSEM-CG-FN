package com.akb.sig.mpnim;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class MPNIM_Client extends Socket implements Runnable {

	private BufferedReader br;
	private PrintStream ps;
	private Scanner user;

	public static void main(String[] args) throws Exception {
		MPNIM_Client mpnim_Client = new MPNIM_Client (args[0]);	
		mpnim_Client.close();
	}

	public MPNIM_Client (String IP) throws IOException {
		super (IP, MPNIM_Server.GAME_PORT);
		br = new BufferedReader (new java.io.InputStreamReader(super.getInputStream()));
		ps = new PrintStream (super.getOutputStream());
		user = new Scanner (System.in);

		new Thread (this, "Client").start();
		// -> siehe Parallel-Funktionale Programmierung || 12inf1.2_Kommunikation_und_Synchronisation_von_Prozessen
	}	


	@Override 
	public void run () {
		// GameLoop
		String readIn = "";
		int check;
		while (super.isConnected()) {
			try {
				readIn = br.readLine();
				while (readIn == null) {
					br = new BufferedReader (new java.io.InputStreamReader(super.getInputStream()));
					readIn = br.readLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(readIn);
			if (readIn.matches("current Marbles:.*"))
				System.out.printf ("How many Marbles would you like to take? [1~3]:\n$ ");
			else if (readIn.matches("player . won!"))
				System.exit(0);
			else if (readIn.matches("you are player .*"))
				continue;

			//check for non-Digit chars
			readIn = user.nextLine();
			if (readIn.matches (".*\\D.*"))
				check = new java.util.Random().nextInt(3)+1;
			else
				check = Integer.parseInt (readIn);
			if (check > 0)
				ps.println (check);
		}
	}
}
