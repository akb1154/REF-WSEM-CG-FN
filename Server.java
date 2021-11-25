import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private static ServerSocket server;

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException{
        server = new ServerSocket(442, 2, InetAddress.getByName("127.0.0.1"));
        Socket s;
        PrintStream ps;

        // read index.html
        String index_html = ""; 
        Scanner sc = new Scanner(new java.io.File("D:\\code\\GitHub\\REF-WSEM-CG-FN\\Beispiele\\index.html"));
        while (sc.hasNextLine())
            index_html += sc.nextLine();
        index_html = index_html.replace("🈷", "?");

        System.out.println(index_html);

        // print to Socket;
        while (true) {
            s = server.accept();
            ps = new PrintStream(s.getOutputStream());
            ps.println("HTTP/1.1 200 OK\r\n\r\n"+index_html);
        }
    }
}