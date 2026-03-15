package model;
import java.util.Random;

// Rappresenta un mostro presente nella caverna
public class Mostro extends BaseEntita {

    // Punti vita del mostro
    private int health;

    // Danno inflitto dal mostro
    private int damage;

    // Generatore di numeri casuali per le statistiche
    Random random = new Random();

    // Costruttore: crea un mostro con vita e danno casuali
    public Mostro(int x, int y) {
        super(x, y);
        this.health = random.nextInt(15) + 5;   // Vita tra 5 e 19
        this.damage = random.nextInt(6) + 2;    // Danno tra 2 e 7
    }

    // Imposta i punti vita aggiornati
    public void setHealth(int health) {
        this.health = health;
    }

    // Restituisce i punti vita
    public int getHealth() {
        return health;
    }

    // Restituisce il danno
    public int getDamage() {
        return damage;
    }
}