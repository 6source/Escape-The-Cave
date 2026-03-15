package model;

// Classe Caverna
public class Caverna {

    // Matrice che rappresenta la mappa
    private static char[][] mappa;

    // Costruttore che inizializza la mappa
    public Caverna(char[][] mappa) {
        this.mappa = mappa;
    }

    // Verifica se la cella è il muro ▓
    public boolean isMuro(int x, int y) {
        return mappa[x][y] == '▓';
    }

    // Verifica se la cella è l'uscita U
    public boolean isUscita(int x, int y) {
        return mappa[x][y] == 'U';
    }

    // Verifica se la cella contiene un mostro grande M
    public boolean isMostro(int x, int y) {
        return mappa[x][y] == 'M';
    }

    // Verifica se la cella contiene un mostro piccolo m
    public boolean isMostroPiccolo(int x, int y) {
        return mappa[x][y] == 'm';
    }

    // Verifica se la cella contiene un tesoro T
    public boolean isTesoro(int x, int y) {
        return mappa[x][y] == 'T';
    }

    // Verifica se la cella contiene un boost e
    public boolean isBoost(int x, int y) {
        return mappa[x][y] == 'e';
    }

    // Rimuove un boost dalla mappa
    public void rimuoviBoost(int x, int y) {
        mappa[x][y] = ' ';
    }

    // Rimuove un mostro dalla mappa
    public void rimuoviMostro(int x, int y) {
        mappa[x][y] = ' ';
    }

    // Rimuove un tesoro dalla mappa
    public void rimuoviTesoro(int x, int y) {
        mappa[x][y] = ' ';
    }

    // Restituisce il carattere presente nella cella
    public char getCella(int x, int y) {
        return mappa[x][y];
    }

    // Restituisce il numero di righe della mappa
    public int getRighe() {
        return mappa.length;
    }

    // Restituisce il numero di colonne della mappa
    public int getColonne() {
        return mappa[0].length;
    }
}