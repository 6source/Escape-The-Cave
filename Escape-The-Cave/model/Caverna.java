package model;
import java.util.ArrayList;
import java.util.List;


public class Caverna {

    private static char[][] mappa;
    private List<Mostro> listaMostri; //lista di mostri

    public Caverna(char[][] mappa) {
        this.mappa = mappa;
        this.listaMostri = new ArrayList<>();
        inizializzaMostri();
    }

    // Scansiona la mappa e crea gli oggetti mostro
    private void inizializzaMostri() {
        for (int i = 0; i < getRighe(); i++) {
            for (int j = 0; j < getColonne(); j++) {
                if (mappa[i][j] == 'M') {
                    listaMostri.add(new Mostro(i, j, 2)
                    );
                } else if (mappa[i][j] == 'm') {
                    listaMostri.add(new Mostro(i, j, 1));
                }
            }
        }
    }
    public List<Mostro> getMostri() {
        return listaMostri;
    }

    // Metodo fondamentale: aggiorna il carattere sulla matrice dopo il movimento
    public void aggiornaPosizioneMostro(int vecchiaX, int vecchiaY, int nuovaX, int nuovaY, char tipo) {
        mappa[vecchiaX][vecchiaY] = ' '; //Pulisce la vecchia cella
        mappa[nuovaX][nuovaY] = tipo;    //mette il mostro nella nuova
    }

    public boolean isLibera(int x, int y) {
        // Una cella è libera se non è un muro e non è l'uscita (o altri ostacoli)
        return !isMuro(x, y) && mappa[x][y] == ' ';
    }

    //Verifica che le coordinate non siano fuori dai bordi della matrice
    public boolean isPosizioneValida(int x, int y) {
        return x >= 0 && x < getRighe() && y >= 0 && y < getColonne();
    }

    //Trasforma la cella indicata in un muro
    public void setMuro(int x, int y) {
        mappa[x][y] = '#';
    }
    //Verifica se la cella è il muro #
    public boolean isMuro(int x, int y) {
        return mappa[x][y] == '#';
    }

    //Verifica se la cella è l'uscita U
    public boolean isUscita(int x, int y) {
        return mappa[x][y] == 'U';
    }

    // Verifica se la cella contiene un tesoro T
    public boolean isTesoro(int x, int y) {
        return mappa[x][y] == 'T';
    } //Il tesoro contiene il timeControl

    // Verifica se la cella contiene un boost e
    public boolean isBoost(int x, int y) {
        return mappa[x][y] == 'e';
    }

    public void rimuoviBoost(int x, int y) {
        mappa[x][y] = ' ';
    }
    public void rimuoviTesoro(int x, int y) {
        mappa[x][y] = ' ';
    }


    public char getCella(int x, int y) {
        return mappa[x][y];
    }
    public int getRighe() {
        return mappa.length;
    }
    public int getColonne() {
        return mappa[0].length;
    }
}