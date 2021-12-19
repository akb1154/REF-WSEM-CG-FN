package com.akb.sig.mpnim;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
		System.out.printf("What server do you want to connect to?\n$ ");
		new Client (new Scanner(System.in).nextLine());
	}
	
	public Client (String IP) throws Exception {
		Socket con = new Socket (IP, 65015);
		Scanner usr = new Scanner (System.in),
				svr = new Scanner (con.getInputStream());
		PrintStream svp = new PrintStream (con.getOutputStream());
		
		while (con.isConnected()) {
			System.out.printf("%s", svr.nextLine());
			svp.println(usr.nextLine());
		}
		
		con.close();
		usr.close();
		svr.close();
		
		System.exit(0x42796521); /// Hex für ASCII: Bye!
	}

}
