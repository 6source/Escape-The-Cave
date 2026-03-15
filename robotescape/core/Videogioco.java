package core;
import model.*;
import java.util.Scanner;

// Classe astratta che definisce la struttura del gioco
// Gestisce il ciclo principale e la logica
public abstract class Videogioco {

    // Mappa di gioco
    protected Caverna caverna;

    // Personaggio controllato dal giocatore
    protected Prigioniero prigioniero;

    // Indica se il gioco è terminato
    protected boolean end = false;

    // Mana inizializzato nel costruttore
    protected int mana;

    // Costruttore: inizializza caverna e prigioniero
    public Videogioco(Caverna caverna, Prigioniero prigioniero, int mana) {
        this.caverna = caverna;
        this.prigioniero = prigioniero;
        this.mana = mana;
    }

    // Metodo per usare mana
    protected boolean usaMana(int quantita) {
        if (mana >= quantita) {
            mana -= quantita;
            return true;
        }
        return false;
    }

    // Metodo per ottenere il mana
    protected int getMana() {
        return mana;
    }

    // Avvia il ciclo principale finché end è false
    public void avvia() {
        while (!end) {
            mostraStato();
            char comando = leggiInput();
            aggiornaGioco(comando);
            controllaVittoria();
            controllaMostro();
            controllaMostroPiccolo();
            controllaVita();
            controllaTesoro();
            controllaBoost();
        }
        mostraRisultato();
    }

    // Aggiorna la logica in base al comando inserito
    protected void aggiornaGioco(char comando) {
        prigioniero.muovi(comando, caverna);
    }

    // Controlla se il prigioniero ha raggiunto l'uscita
    protected void controllaVittoria() {
        if (caverna.isUscita(prigioniero.getX(), prigioniero.getY())) {
            end = true;
        }
    }

    // Controlla se il giocatore trova un tesoro
    protected void controllaTesoro() {
        int x = prigioniero.getX();
        int y = prigioniero.getY();

        if (caverna.isTesoro(x, y)) {

            System.out.println("\n✨═══════════════════════════════════════✨");
            System.out.println("          🏆 TESORO RUBATO🏆");
            System.out.println("✨═══════════════════════════════════════✨");

            System.out.println("Hai scoperto un antico tesoro nascosto!");
            System.out.println("🔓 Nuovo Rank sbloccato:  EPICO");
            System.out.println("⚔️  Danni aumentati!");
            System.out.println("❤️  Punti vita aumentati!");
            System.out.println("Da adesso in poi i nemici che incontrerai saranno sempre più forti!");

            caverna.rimuoviTesoro(x, y);
        }
    }
    boolean infliggiDannoFuoco = false; //Variabile per l'abilità "Fireball"

    // Controlla se c'è un mostro grande nelle celle circostanti
    protected void controllaMostro() {

        int x = prigioniero.getX();
        int y = prigioniero.getY();

        if (caverna.isMostro(x + 1, y) ||
                caverna.isMostro(x - 1, y) ||
                caverna.isMostro(x, y + 1) ||
                caverna.isMostro(x, y - 1)) {

            int mx = 0, my = 0;

            // Individua la posizione precisa del mostro
            if (caverna.isMostro(x + 1, y)) { mx = x + 1; my = y; }
            else if (caverna.isMostro(x - 1, y)) { mx = x - 1; my = y; }
            else if (caverna.isMostro(x, y + 1)) { mx = x; my = y + 1; }
            else if (caverna.isMostro(x, y - 1)) { mx = x; my = y - 1; }

            Mostro mostro = new Mostro(mx, my);

            System.out.println("\n⚠️═══════════════════════════════════════");
            System.out.println("        MOSTRO NELLE VICINANZE!");
            System.out.println("═══════════════════════════════════════⚠️");

            System.out.println("\n STATO ATTUALE:");
            System.out.println("\n🧍 TU:");
            System.out.println("❤️  Vita: " + prigioniero.getHealth());
            System.out.println("⚔️  Danno: " + prigioniero.getDamage());

            System.out.println("\n👹 MOSTRO:");
            System.out.println("❤️  Vita: " + mostro.getHealth());
            System.out.println("💢 Danno: " + mostro.getDamage());

            Scanner sc = new Scanner(System.in);
            String scelta;

            do {
                System.out.println("\nVuoi affrontarlo? (si/no)");
                System.out.print("➤ ");
                scelta = sc.nextLine();

                if (scelta.equalsIgnoreCase("si")) {
                    if(mana > 0){
                        do{
                            System.out.println("Vuoi utilizzare l'abilità: Fireball?");
                            scelta = sc.nextLine();

                            if(scelta.equalsIgnoreCase("si") && usaMana(2)){
                                infliggiDannoFuoco = true;
                            }
                        }while(!scelta.equalsIgnoreCase("si") && !scelta.equalsIgnoreCase("no"));

                    }
                    avviaCombattimentoMostro(mx, my);

                } else if (scelta.equalsIgnoreCase("no")) {
                    System.out.println("\nTi allontani lentamente cercando di non attirare la sua attenzione...");
                } else {
                    System.out.println("\nOutput sbagliato!");
                }

            } while (!scelta.equalsIgnoreCase("si") && !scelta.equalsIgnoreCase("no"));
        }
    }

    // Gestisce il combattimento contro un mostro grande
    private void avviaCombattimentoMostro(int x, int y) {

        Mostro mostro = new Mostro(x, y);

        System.out.println("\n═══════════════════════════════════════");
        System.out.println("      COMBATTIMENTO INIZIATO!");
        System.out.println("═══════════════════════════════════════");
        if(infliggiDannoFuoco){
            mostro.setHealth(mostro.getHealth() - FireBall.getDamage()); //Danno iniziale preso dalla fireball
            infliggiDannoFuoco = false;
            System.out.println("️ ️Hai inflitto 8 di danno con la Fireball!!\uD83D\uDD25 " + mostro.getHealth());

        }else{
            System.out.println("HP Mostro: " + mostro.getHealth());
        }
        System.out.println("HP Giocatore: " + prigioniero.getHealth());
        System.out.println("═══════════════════════════════════════\n");

        while (mostro.getHealth() > 0 && prigioniero.getHealth() > 0) {

            int dannoPrigioniero = prigioniero.getDamage();
            int dannoMostro = mostro.getDamage();

            mostro.setHealth(mostro.getHealth() - dannoPrigioniero);
            prigioniero.setHealth(prigioniero.getHealth() - dannoMostro);

            System.out.println("⚔️  Hai attaccato il mostro e gli hai inflitto " + dannoPrigioniero + " danni!");
            System.out.println("💢 Il mostro ti ha colpito infliggendoti " + dannoMostro + " danni!");

            System.out.println("\n📊 STATO ATTUALE:");
            System.out.println("❤️  Tua vita: " + prigioniero.getHealth());
            System.out.println("👹 Vita mostro: " + mostro.getHealth());
            System.out.println("───────────────────────────────────────\n");
        }

        if (mostro.getHealth() <= 0) {
            System.out.println("\n🎉═══════════════════════════════════════");
            System.out.println("      MOSTRO SCONFITTO!");
            System.out.println("═══════════════════════════════════════🎉");
            System.out.println("Hai guadagnato esperienza!");
            System.out.println("⬆️  LEVEL UPP!");
            caverna.rimuoviMostro(x, y);
            prigioniero.aumentaLivello(false);
        }

        if (prigioniero.getHealth() <= 0) {
            System.out.println("\n💀═══════════════════════════════════════");
            System.out.println("           NOOO HAI PERSOOO :(((");
            System.out.println("═══════════════════════════════════════💀\n");
        }
    }

    // Controlla se il giocatore è morto
    protected void controllaVita() {
        if (prigioniero.getHealth() <= 0) {
            System.out.println("\n☠️═══════════════════════════════════════");
            System.out.println("               GAME OVER");
            System.out.println("═══════════════════════════════════════☠️\n");
            end = true;
        }
    }

    // Controlla presenza mostro piccolo
    protected void controllaMostroPiccolo() {

        int x = prigioniero.getX();
        int y = prigioniero.getY();

        if (caverna.isMostroPiccolo(x + 1, y) ||
                caverna.isMostroPiccolo(x - 1, y) ||
                caverna.isMostroPiccolo(x, y + 1) ||
                caverna.isMostroPiccolo(x, y - 1)) {

            int mx = 0;
            int my = 0;

            if (caverna.isMostroPiccolo(x + 1, y)) { mx = x + 1; my = y; }
            else if (caverna.isMostroPiccolo(x - 1, y)) { mx = x - 1; my = y; }
            else if (caverna.isMostroPiccolo(x, y + 1)) { mx = x; my = y + 1; }
            else if (caverna.isMostroPiccolo(x, y - 1)) { mx = x; my = y - 1; }

            MostroPiccolo mostroPiccolo = new MostroPiccolo(mx, my);

            System.out.println("\n⚠️═══════════════════════════════════════");
            System.out.println("        MOSTRO NELLE VICINANZE!");
            System.out.println("═══════════════════════════════════════⚠️");

            System.out.println("\n🧍 TU:");
            System.out.println("❤️  Vita: " + prigioniero.getHealth());
            System.out.println("⚔️  Danno: " + prigioniero.getDamage());

            System.out.println("\n👹 MOSTRO:");
            System.out.println("❤️  Vita: " + mostroPiccolo.getHealth());
            System.out.println("💢 Danno: " + mostroPiccolo.getDamage());

            Scanner sc = new Scanner(System.in);
            String scelta;
            do {
                System.out.println("\nVuoi affrontarlo? (si/no)");
                System.out.print("➤ ");
                scelta = sc.nextLine();

                if (scelta.equalsIgnoreCase("si")) {
                    if(mana > 0){
                        do{
                            System.out.println("Vuoi utilizzare l'abilità: Fireball?");
                            scelta = sc.nextLine();

                            if(scelta.equalsIgnoreCase("si") && usaMana(2)){
                                infliggiDannoFuoco = true;
                            }
                        }while(!scelta.equalsIgnoreCase("si") && !scelta.equalsIgnoreCase("no"));
                    }

                    avviaCombattimentoMostroPiccolo(mx, my);

                } else if (scelta.equalsIgnoreCase("no")) {
                    System.out.println("\nTi allontani lentamente cercando di non attirare la sua attenzione...");
                } else {
                    System.out.println("\nOutput sbagliato!");
                }
            } while (!scelta.equalsIgnoreCase("si") && !scelta.equalsIgnoreCase("no"));
        }
    }

    // Combattimento contro mostro piccolo
    private void avviaCombattimentoMostroPiccolo(int x, int y) {

        MostroPiccolo mostroPiccolo = new MostroPiccolo(x, y);

        System.out.println("\n═══════════════════════════════════════");
        System.out.println("      COMBATTIMENTO INIZIATO!");
        System.out.println("═══════════════════════════════════════");
        if(infliggiDannoFuoco){
            mostroPiccolo.setHealth(mostroPiccolo.getHealth() - FireBall.getDamage()); //Danno iniziale preso dalla fireball
            infliggiDannoFuoco = false;
            System.out.println("️️Hai inflitto 8 di danno con la Fireball!!\uD83D\uDD25 " + mostroPiccolo.getHealth());

        }else{
            System.out.println("HP Mostro: " + mostroPiccolo.getHealth());
        }
        System.out.println("HP Giocatore: " + prigioniero.getHealth());
        System.out.println("═══════════════════════════════════════\n");

        while (mostroPiccolo.getHealth() > 0 && prigioniero.getHealth() > 0) {

            int dannoPrigioniero = prigioniero.getDamage();
            int dannoMostro = mostroPiccolo.getDamage();

            mostroPiccolo.setHealth(mostroPiccolo.getHealth() - dannoPrigioniero);
            prigioniero.setHealth(prigioniero.getHealth() - dannoMostro);
        }

        if (mostroPiccolo.getHealth() <= 0) {
            caverna.rimuoviMostro(x, y);
            prigioniero.aumentaLivello(true);
        }

        if (prigioniero.getHealth() <= 0) {
            System.out.println("\n💀 SEI STATO SCONFITTO\n");
        }
    }

    // Bonus insetti pallina
    protected void controllaBoost() {
        int x = prigioniero.getX();
        int y = prigioniero.getY();

        if (caverna.isBoost(x, y)) {
            System.out.println("⬆️  Vita incrementata di 2 hp!");
            caverna.rimuoviBoost(x, y);
            prigioniero.healthBuff();
            prigioniero.damageBuff();
        }
    }

    // Metodo astratto per leggere input
    protected abstract char leggiInput();

    // Metodo astratto per mostrare stato
    protected abstract void mostraStato();

    // Metodo astratto per mostrare risultato finale
    protected abstract void mostraRisultato();
}