package Server;

import java.io.*;
import java.net.*;

public class MainServer {
    public static void main(String[] args) {
        System.out.println("Server in esecuzione");

        try (ServerSocket server = new ServerSocket(3000)) {
            while (true) {
                System.out.println("\nIn attesa di una connessione...");

                try (Socket clientSocket = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("Client connesso: " + clientSocket.getInetAddress());
//

                    String messaggio = in.readLine();
                    if (messaggio != null) {
                        System.out.println("Messaggio ricevuto: " + messaggio);
                    }

                    PrintWriter pw =new PrintWriter(clientSocket.getOutputStream());
                    pw.println("Ciao");
                    pw.flush();

                } catch (IOException e) {
                    System.out.println("Errore con un client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Errore del Server: " + e.getMessage());
        }
    }
}//