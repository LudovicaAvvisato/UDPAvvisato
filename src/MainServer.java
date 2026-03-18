
import java.io.*;
import java.net.*;

public class MainServer {
    public static void main(String[] args) {
        int port = 6789;

        try (DatagramSocket dSocket = new DatagramSocket(port)) {
            System.out.println("Server in ascolto sulla porta " + port + "...");

            while (true) {
                byte[] bufferIn = new byte[256];

                // Ricezione
                DatagramPacket inPacket = new DatagramPacket(bufferIn, bufferIn.length);
                dSocket.receive(inPacket);

                InetAddress clientAddress = inPacket.getAddress();
                int clientPort = inPacket.getPort();
                String messageIn = new String(inPacket.getData(), 0, inPacket.getLength());

                System.out.println("CLIENT [" + clientAddress + ":" + clientPort + "]> " + messageIn);

                // Risposta (Echo)
                String response = "Messaggio ricevuto: " + messageIn;
                byte[] bufferOut = response.getBytes();
                DatagramPacket outPacket = new DatagramPacket(bufferOut, bufferOut.length, clientAddress, clientPort);
                dSocket.send(outPacket);
            }

        } catch (BindException e) {
            System.err.println("Errore: porta già in uso.");
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
        }
    }
}