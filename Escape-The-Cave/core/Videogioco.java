package core;
import java.util.Random;
import java.util.List;
import model.*;
import gui.GiocoGUI;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Videogioco {

    Random rand = new Random();
    protected Caverna caverna;
    protected Prigioniero prigioniero;
    protected boolean end = false;
    protected boolean WL = false;
    protected boolean giocoAvviato = false; // Indica se il giocatore ha fatto la prima mossa
    private boolean godMode = false;  //Serve per il timeStop

    // Timer per gestire il movimento autonomo dei mostri
    protected static Timer timerMostri;

    public Videogioco(Caverna caverna, Prigioniero prigioniero) {
        this.caverna = caverna;
        this.prigioniero = prigioniero;

        // Inizializza il timer: i mostri si muovono ogni 280ms
        gestisciTimerMostri(280);
    }

    public static void mettiInPausa() {
        if (timerMostri != null) {          //Serve quando mi arriva un pop up per non far continuare a camminare i mostri prima che metto OK
            timerMostri.stop();
        }
    }

    public static void riprendi() {
        if (timerMostri != null) {
            timerMostri.start();
        }
    }

    private void gestisciTimerMostri(int intervallo) {
        timerMostri = new Timer(intervallo, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!end) {
                    gestisciTurnoMostri();
                    aggiornaPannello();
                    controllaVita();
                } else {
                    timerMostri.stop();
                }
            }
        });
    }

    public void eseguiTurno(char comando) {
        if (!end) {
            if (!giocoAvviato) {
                giocoAvviato = true;
                timerMostri.start();
            }

            mossaGiocatore(comando);
            controllaVittoria();
            controllaVita();
            controllaTempo();
            controllaBoost();
        }
    }
    protected void mossaGiocatore(char comando) { // Serve a controllare le collisioni
        // 1. Muovi il prigioniero
        prigioniero.muovi(comando, caverna);
    }


    protected void gestisciTurnoMostri() {
        //Esegui il ciclo di movimento SOLO SE il GodMode non è attivo
        if (!isGodMode()) {
            List<Mostro> listaMostri = caverna.getMostri();
            for (int i = 0; i < listaMostri.size(); i++) {
                Mostro m = listaMostri.get(i); //Prendi il mostro in i
                muoviMostroConPathfinding(m);  //Esegui l'azione
            }
        }
    }

    private void muoviMostroConPathfinding(Mostro m) {
        int startX = m.getX();
        int startY = m.getY(); //mostro
        int targetX = prigioniero.getX();
        int targetY = prigioniero.getY(); //prigioniero

        if (startX == targetX && startY == targetY) {
            //se il mostro è sopra il player e non è attiva la GodMode fa infligge danno
            if (!isGodMode()) {
                prigioniero.setHealth(prigioniero.getHealth() - m.getDamage());
            }
        } else {
            //se il mostro non è sul player calcola la prossima mossa ottimale usando il metodo per calcolarlo (BFS)
            int[] prossimaMossa = calcolaDirezioneBFS(startX, startY, targetX, targetY);
            if (prossimaMossa != null) {
                int nextX = prossimaMossa[0]; //ha la x
                int nextY = prossimaMossa[1]; //ha la y

                char tipo = caverna.getCella(startX, startY); //vede che tipo di carattere è sopra
                m.setPosizione(nextX, nextY);
                caverna.aggiornaPosizioneMostro(startX, startY, nextX, nextY, tipo);

                if (nextX == targetX && nextY == targetY && !isGodMode()) {
                    prigioniero.setHealth(prigioniero.getHealth() - m.getDamage());
                }
            }
        }
    }

    protected void controllaVittoria() {
        if (caverna.isUscita(prigioniero.getX(), prigioniero.getY())) {
            end = true;
            WL = true;
            if (timerMostri != null) timerMostri.stop();
        }
    }
    public boolean isGodMode() { return godMode; }
    public void setGodMode(boolean active) { this.godMode = active; }

    protected void controllaTempo() {
        int x = prigioniero.getX();
        int y = prigioniero.getY();

        if (caverna.isTesoro(x, y)) { //Non metto un messaggio di informazione
            caverna.rimuoviTesoro(x, y);
            attivaPowerUpTesoro();
        }
    }
    private void attivaPowerUpTesoro() {
        setGodMode(true); //la attiva
        Prigioniero.usaMana(4);
        //timer di 2,2 secondi
        Timer timerTesoro = new Timer(2250, e -> {
            setGodMode(false); //La reimposta ad off quando scade il tempo
        });
        timerTesoro.start();
    }
    protected void controllaVita() {
        if (prigioniero.getHealth() <= 0) {
            end = true;
            if (timerMostri != null) timerMostri.stop();
        }
    }

    protected void controllaBoost() {
        int x = prigioniero.getX();
        int y = prigioniero.getY();
        int n = rand.nextInt(2);
        if (caverna.isBoost(x, y)) {
            caverna.rimuoviBoost(x, y);
            if(n == 0){
                prigioniero.setAggiungiMuro();
                Videogioco.mettiInPausa();
                GiocoGUI.informazioneMuro();
                Videogioco.riprendi();

            }else{
                prigioniero.healthBuff();
            }
        }
    }
    //algoritmo BFS per trovare il varco nel muro e raggiungere il player
    private int[] calcolaDirezioneBFS(int sX, int sY, int tX, int tY) {
        int righe = caverna.getRighe();
        int colonne = caverna.getColonne(); //prende le dimensioni della mappa
        int[] prossimaMossa = null;
        boolean trovato = false; //interrompe il ciclo quando troviamo il player

        Point[][] parent = new Point[righe][colonne]; //serve a memorizzare la cella di provenienza di ogni punto
        Queue<Point> coda = new LinkedList<>(); //coda per gestire l'ordine in cui esplorare le celle

        coda.add(new Point(sX, sY));
        parent[sX][sY] = new Point(-1, -1);

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, 1, -1};

        while (!coda.isEmpty() && !trovato) {
            Point attuale = coda.poll(); //richiama la cella iniziale

            // Controllo se la cella attuale è la posizione del prigioniero
            if (attuale.x == tX && attuale.y == tY) {
                trovato = true;
                Point passo = attuale; //parte dal player per ricostruire il sentiero da prendere

                //risale  da parent finché non trova la cella del mostro
                while (parent[passo.x][passo.y].x != sX || parent[passo.x][passo.y].y != sY) {
                    passo = parent[passo.x][passo.y];
                }
                prossimaMossa = new int[]{passo.x, passo.y}; // Memorizza le coordinate della mossa da compiere
            }

            //se non ha ancora trovato il player, esplora le 4 celle adiacenti
            for (int i = 0; i < 4 && !trovato; i++) {
                int nr = attuale.x + dr[i];
                int nc = attuale.y + dc[i];

                //verifica che la cella sia nei limiti non è visitata ed è attravers
                if (nr >= 0 && nr < righe && nc >= 0 && nc < colonne && parent[nr][nc] == null) {
                    // Il mostro procede se la cella è vuota o se contiene il player
                    if (caverna.isLibera(nr, nc) || (nr == tX && nc == tY)) {
                        parent[nr][nc] = attuale; // Salva la cella attuale come "genitore" della vicina
                        coda.add(new Point(nr, nc)); // Aggiunge la vicina alla coda per esplorarla in seguito
                    }
                }
            }
        }
        return prossimaMossa; // Unico punto di uscita: restituisce le coordinate o null se non c'è percorso
    }
    protected abstract void aggiornaPannello();
}