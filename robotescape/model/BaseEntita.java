package model;

// Classe astratta che rappresenta un'entità del gioco
// Viene estesa da personaggi come Prigioniero e Mostro
public class BaseEntita {

    // Coordinata orizzontale
    protected int x;

    // Coordinata verticale
    protected int y;

    // Costruttore: crea un'entità nella posizione indicata
    public BaseEntita(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Restituisce la coordinata x
    public int getX() {
        return x;
    }

    // Restituisce la coordinata y
    public int getY() {
        return y;
    }
}