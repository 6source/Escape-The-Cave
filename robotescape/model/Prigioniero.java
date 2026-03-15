package model;

// Rappresenta il personaggio controllato dal giocatore
public class Prigioniero extends BaseEntita {

    // Punti vita del personaggio
    private static int health;

    // Danno inflitto dal personaggio
    private int damage;

    // Livello attuale
    private int level;

    // Indica se l'aumento di livello è legato a un mostro
    private boolean indicaMostro;

    // Costruttore: inizializza posizione, vita, danno e livello iniziale
    public Prigioniero(int x, int y, int health, int damage) {
        super(x, y);
        this.health = health;
        this.damage = damage;
        this.level = 1;
    }

    // ===== SETTER =====
    public void setHealth(int health) {
        this.health = health;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    // ===== GETTER =====
    public static int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getLevel() {
        return level;
    }

    // Muove il prigioniero nella direzione indicata (WASD) se la cella è valida
    public void muovi(char comando, Caverna caverna) {
        int nuovoX = x;
        int nuovoY = y;

        switch (comando) {
            case 'w': case 'W':
                nuovoX--;   // su
                break;
            case 's': case 'S':
                nuovoX++;   // giù
                break;
            case 'a': case 'A':
                nuovoY--;   // sinistra
                break;
            case 'd': case 'D':
                nuovoY++;   // destra
                break;
        }

        // Controlla che la nuova posizione sia dentro i limiti e non sia un muro
        if (nuovoX >= 0 && nuovoX < caverna.getRighe() &&
                nuovoY >= 0 && nuovoY < caverna.getColonne() &&
                !caverna.isMuro(nuovoX, nuovoY)) {

            x = nuovoX;
            y = nuovoY;
        }
    }

    // Aumenta il livello del personaggio e applica bonus diversi
    public void aumentaLivello(boolean indicaMostro){
        if(indicaMostro == false){
            level++;
            damage += 2;
            health += 5;
            System.out.println("Sei salito al livello " + level + "!");
            System.out.println("⬆️ Vita attuale: " + health + " ⬆️ Danno: " + damage);
        } else {
            level++;
            health += 2;
            System.out.println("Sei salito al livello " + level + "!");
            System.out.println("⬆️ Vita attuale: " + health + " ⬆️ Danno: " + damage);
        }
    }

    // Aumenta leggermente la salute
    public void healthBuff(){
        health += 2;
        System.out.println("⬆️ La tua salute è arrivata a " + health + "!");
    }

    // Aumenta leggermente il danno
    public void damageBuff(){
        damage += 1;
        System.out.println("⬆️ Il tuo danno è arrivato a " + damage + "!");
    }
}