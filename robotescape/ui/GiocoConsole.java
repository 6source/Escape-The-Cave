package ui;
import java.util.Scanner;
import core.Videogioco;
import model.Caverna;
import model.Prigioniero;

public class GiocoConsole extends Videogioco {

    // Oggetto per la lettura dell'input da tastiera
    Scanner scanner = new Scanner(System.in);

    // Costruttore
    public GiocoConsole(Caverna caverna, Prigioniero prigioniero, int mana) {
        super(caverna, prigioniero, mana);
    }

    // Legge un comando dalla console e restituisce il primo carattere inserito
    protected char leggiInput() {
        System.out.println("Vita attuale ❤\uFE0F: " + prigioniero.getHealth());
        System.out.println("Mana rimanente ⚗\uFE0F: " + getMana());
        System.out.println("Comandi (W A S D): ");
        String input = scanner.nextLine();
        return input.charAt(0);
    }

    // Mostra la mappa della caverna con la posizione attuale del prigioniero
    protected void mostraStato() {
        for (int x = 0; x < caverna.getRighe(); x++) {
            for (int y = 0; y < caverna.getColonne(); y++) {

                // Se la posizione corrente corrisponde a quella del prigioniero, stampa "P"
                if (x == prigioniero.getX() && y == prigioniero.getY()) {
                    System.out.print("P ");
                } else {
                    // Altrimenti stampa il contenuto della cella
                    System.out.print(caverna.getCella(x, y) + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Mostra il messaggio finale di vittoria
    protected void mostraRisultato() {
        System.out.println("Sei uscito");
    }
}