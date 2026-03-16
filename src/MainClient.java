package Client;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        Scanner tastiera = new Scanner(System.in);

        while (true) {
            System.out.print("Inserisci messaggio da inviare: ");
            String testo = tastiera.nextLine();

            if (testo.equalsIgnoreCase("")) break;

            try (Socket socket = new Socket("localhost", 3000);
                 PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {

                pw.println(testo);
                pw.flush();
                System.out.println("Inviato correttamente.");

                //Blocco di lettura

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String messaggio = in.readLine();
                if (messaggio != null) {
                    System.out.println(messaggio);
                }

            } catch (IOException e) {
                System.out.println("Server non raggiungibile. Assicurati che il Server sia avviato.");
                break;
            }
        }
        tastiera.close();
        System.out.println("Client chiuso.");
    }
}//