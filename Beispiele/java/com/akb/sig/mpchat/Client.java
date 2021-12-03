import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

    private static final int PORT = 38174;

    private Socket server;
    private PrintStream serverOut;
    private BufferedReader serverIn;

    public Client (String IP, String Username) throws IOException {
        server = new Socket (IP, PORT);
        serverOut = new PrintStream(server.getOutputStream(), false, "utf-8");
        serverIn = new BufferedReader(new java.io.InputStreamReader(server.getInputStream()));

        String response;
        //register User and quit on error
        serverOut.printf("register %s\n", Username);
        response = serverIn.readLine();
        if (response == "200 OK")
            System.out.println("joined "+IP+" as "+Username);
        else
            System.err.println("Something went wrong\n[DEBUG]RESPONSE$ "+response);
        if (response != "200 OK")
            System.exit(-1);
    }


}
