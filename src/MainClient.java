import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        int port = 6789; // Porta del server
        byte[] buffer = new byte[256];

        try (DatagramSocket dSocket = new DatagramSocket();
             Scanner sc = new Scanner(System.in)) {

            InetAddress serverAddress = InetAddress.getLocalHost();
            System.out.println("Indirizzo del server trovato: " + serverAddress);

            System.out.print("Inserisci il messaggio da inviare: ");
            String message = sc.nextLine();

            // Invio del pacchetto
            DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(), serverAddress, port);
            dSocket.send(outPacket);

            // Ricezione della risposta
            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
            dSocket.receive(inPacket);

            String response = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println("Risposta dal server: " + response);

        } catch (UnknownHostException e) {
            System.err.println("Errore DNS: " + e.getMessage());
        } catch (SocketException e) {
            System.err.println("Errore Socket: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
        }

        System.out.println("Comunicazione chiusa!");
    }
}