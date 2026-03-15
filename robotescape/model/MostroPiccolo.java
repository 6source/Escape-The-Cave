package model;
import java.util.Random;

// Rappresenta un mostro piccolo presente nella caverna
public class MostroPiccolo extends BaseEntita {
    // Vita del mostro
    private int health;

    // Danno del mostro
    private int damage;

    // Generatore di numeri casuali
    Random random = new Random();

    // Costruisce un mostro piccolo con statistiche casuali (più basse rispetto al normale)
    public MostroPiccolo(int x, int y) {
        super(x, y);
        this.health = random.nextInt(7) + 3;  // punti vita tra 3 e 9
        this.damage = random.nextInt(2) + 2;  // danno tra 2 e 3
    }

    // Aggiorna i punti vita
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