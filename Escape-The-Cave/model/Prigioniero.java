package model;

import javax.swing.*;
import gui.GiocoGUI;
import core.Videogioco;

// Rappresenta il personaggio controllato dal giocatore
public class Prigioniero extends BaseEntita {

    // Punti vita del personaggio
    public static int mana;
    private static int health;
    private int piazzaMuro;
    private char ultimaDirezione = 's';
    // Costruttore: inizializza posizione, vita, danno e livello iniziale
    public Prigioniero(int x, int y, int health, int piazzaMuro) {
        super(x, y);
        this.health = health;
        this.piazzaMuro = piazzaMuro;
        this.mana = 6;
    }

    // ===== SETTER =====
    public void setHealth(int health) {
        this.health = health;
    }

    // ===== GETTER =====
    public static int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public static boolean usaMana(int quantita) { // static perchè viene richiamata a prigioniero
        if (mana >= quantita) {
            mana -= quantita;
            return true;
        }
        return false;
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
            case 'e': case 'E':
                piazzaMuro(caverna); // abilità
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

    // Aumenta la salute
    public void healthBuff(){
        health += 1;
    }

    public void setAggiungiMuro() {
        this.piazzaMuro++;
    }

    public void piazzaMuro(Caverna caverna) {
        if(piazzaMuro > 0){
            int xMuro = this.x;
            int yMuro = this.y;

            // Determina la posizione dietro al giocatore in base all'ultimo tasto
            switch (ultimaDirezione) {
                case 'w': case 'W': yMuro -= 1; break; //Se andava su, muro sotto
                case 's': case 'S': yMuro += 1; break; // Se andava giù, muro sopra
                case 'a': case 'A': xMuro -= 1; break; // Se andava  sx, muro a destra
                case 'd': case 'D': xMuro += 1; break; //Se andava a dx, muro a sinistra
            }

            // Piazza il muro solo se la cella è valida e libera (non sopra mostri o player)
            if(caverna.isPosizioneValida(xMuro, yMuro) && caverna.isLibera(xMuro, yMuro)) {
                caverna.setMuro(xMuro, yMuro);
                piazzaMuro--; // lo decremento solo se lo piazzo

                Prigioniero.usaMana(2);
            }
        }else{
            Videogioco.mettiInPausa();

            GiocoGUI.informazioneAbilità();

            // Chiediamo al gioco di ripartire
            Videogioco.riprendi();
        }
    }
}