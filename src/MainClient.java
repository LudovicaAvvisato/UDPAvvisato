import java.io.*;
import java.net.*;
import java.util.*;
public class MainClient {
    public static void main(String[] args) {
        String serverIp = "localhost";
        int serverPort = 6789;
        String mGroup = "230.0.0.1";
        int mPort = 6790;

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            clientSocket.setSoTimeout(10000);
            InetAddress srvAddr = InetAddress.getByName(serverIp);

            // PREPARAZIONE: Apriamo la socket multicast PRIMA di iniziare
            try (MulticastSocket mSocket = new MulticastSocket(mPort)) {
                InetAddress group = InetAddress.getByName(mGroup);
                mSocket.joinGroup(group);

                // 1. ECHO
                String msg = "Test Connessione";
                clientSocket.send(new DatagramPacket(msg.getBytes(), msg.length(), srvAddr, serverPort));
                byte[] buf = new byte[1024];
                DatagramPacket pIn = new DatagramPacket(buf, buf.length);
                clientSocket.receive(pIn);
                System.out.println("Il Server è online: " + new String(pIn.getData(), 0, pIn.getLength()));

                // 2. ATTESA MULTICAST
                System.out.println("In attesa del segnale multicast...");
                byte[] mBuf = new byte[256];
                DatagramPacket mPacket = new DatagramPacket(mBuf, mBuf.length);
                mSocket.receive(mPacket);
                System.out.println("Segnale ricevuto: " + new String(mPacket.getData(), 0, mPacket.getLength()));
                mSocket.leaveGroup(group);
            }

            // ... dopo mSocket.leaveGroup(group);

            Scanner in = new Scanner(System.in);
            System.out.println("\nINSERIMENTO NUOVO STUDENTE");

            System.out.print("Nome: ");
            String nome = in.nextLine();

            System.out.print("Cognome: ");
            String cognome = in.nextLine();

            System.out.print("Matricola (numero): ");

            int matricola = 0;
            try {
                matricola = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Matricola non valida, imposto 0 di default.");
            }

// Ora creiamo l'oggetto con i dati inseriti
            Studente s = new Studente(nome, cognome, matricola);

// ... procedi con l'invio (baos, ObjectOutputStream, etc.)

            // 3. INVIO STUDENTE
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(s);
            oos.flush();
            byte[] datiStrutturati = baos.toByteArray();

            System.out.println("Invio dello studente");
            clientSocket.send(new DatagramPacket(datiStrutturati, datiStrutturati.length, srvAddr, serverPort));

            // 4. RICEZIONE CONFERMA
            byte[] lastBuf = new byte[1024];
            DatagramPacket pLast = new DatagramPacket(lastBuf, lastBuf.length);
            clientSocket.receive(pLast);
            System.out.println("Conferma finale: " + new String(pLast.getData(), 0, pLast.getLength()));

        } catch (Exception e) {
            System.err.println("Errore " + e.getMessage());
            e.printStackTrace();
        }
    }
}