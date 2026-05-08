package model;

// Rappresenta un mostro presente nella caverna
public class Mostro extends BaseEntita {

    // Danno inflitto dal mostro
    private int damage;
    private boolean attivo = false;

    public Mostro(int x, int y, int damage) {
        super(x, y);
        this.damage = damage;
    }

    // Restituisce il danno
    public int getDamage() {
        return damage;
    }

    // Serve per aggiornare le coordinate X e Y
    public void setPosizione(int nuovaX, int nuovaY) {
        this.x = nuovaX;
        this.y = nuovaY;
    }
}