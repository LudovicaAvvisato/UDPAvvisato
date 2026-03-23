import java.io.Serializable;

public class Studente implements Serializable {
    //private static final long serialVersionUID = 1L;
    public String nome;
    public String cognome;
    public int matricola;

    public Studente(String nome, String cognome, int matricola) {
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
    }
}