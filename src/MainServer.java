import java.io.*;
import java.net.*;

public class MainServer {
    public static void main(String[] args) {
        int port = 6789;
        String mGroup = "230.0.0.1";
        int mPort = 6790;

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.out.println("Il server è in ascolto sulla porta " + port);

            // 1. ECHO
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(packet);
            //System.out.println("[SERVER] Ricevuto Echo, rispondo...");
            serverSocket.send(new DatagramPacket(packet.getData(), packet.getLength(), packet.getAddress(), packet.getPort()));

            // Piccola pausa per dare tempo al client di mettersi in ascolto multicast
            Thread.sleep(500);

            // 2. MULTICAST
            InetAddress group = InetAddress.getByName(mGroup);
            String alert = "Invio Dati";
            byte[] alertMsg = alert.getBytes();
            serverSocket.send(new DatagramPacket(alertMsg, alertMsg.length, group, mPort));
            System.out.println("Il segnale Multicast è stato inviato.");

            // 3. RICEZIONE OGGETTO
            serverSocket.receive(packet); // Riceve il pacchetto dello studente

            // IMPORTANTE: Creiamo il buffer esatto per i dati ricevuti
            byte[] data = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), 0, data, 0, packet.getLength());

            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                Studente s = (Studente) ois.readObject();
                System.out.println("Studente: " + s.nome + " " + s.cognome + " " + s.matricola);
            }

            // 4. RISPOSTA FINALE
            String risposta = "Ricezione completata correttamente.";
            byte[] respBuf = risposta.getBytes();
            serverSocket.send(new DatagramPacket(respBuf, respBuf.length, packet.getAddress(), packet.getPort()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}