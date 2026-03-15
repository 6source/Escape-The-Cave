package core;
import ui.*;
import model.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // TITOLO ASCII
        System.out.println("""
           ___                              __                      ____    _                      ___                            
          F __".  _    _    _ ___   _    _  LJ  _    _   ____      /_  _\\  FJ___      ____       ,"___".    ___ _  _    _   ____  
         J (___| J |  | L  J '__ ",J |  | L    J |  | L F __ J     [J  L] J  __ `.   F __ J      FJ---L]   F __` LJ |  | L F __ J 
         J\\___ \\ | |  | |  | |__|-JJ J  F L FJ J J  F L| _____J     |  |  | |--| |  | _____J    J |   LJ  | |--| |J J  F L| _____J
        .--___) \\F L__J J  F L  `-'J\\ \\/ /FJ  LJ\\ \\/ /FF L___--.    F  J  F L  J J  F L___--.   | \\___--. F L__J JJ\\ \\/ /FF L___--.
        J\\______J\\____,__LJ__L      \\\\__// J__L \\\\__//J\\______/F   J____LJ__L  J__LJ\\______/F   J\\_____/FJ\\____,__L\\\\__//J\\______/F
         J______FJ____,__F|__L       \\__/  |__|  \\__/  J______F    |____||__L  J__| J______F     J_____F  J____,__F \\__/  J______F
        """);

        // MENU PRINCIPALE
        System.out.println("====================================================");
        System.out.println("|                  MENU PRINCIPALE                 |");
        System.out.println("====================================================");
        System.out.println("| 1 | Gioca                                        |");
        System.out.println("| 2 | Comandi e Informazioni                       |");
        System.out.println("====================================================");
        System.out.println("Scelta: ");

        int scelta = scanner.nextInt();
        scanner.nextLine();
        // Pulisce il buffer
        switch (scelta) {
            case 1:
                avviaGioco();
                break;
            case 2:
                mostraComandi();
                // Chiede se vuole giocare
                System.out.print("Vuoi iniziare a giocare? (si/no): ");
                String risposta = scanner.nextLine();
                if (risposta.equalsIgnoreCase("si")) avviaGioco();
                else System.out.println("Uscita dal gioco");
                break;
            default:
                System.out.println("| Scelta non valida                                |");
                System.out.println("====================================================");
        }
    }

    public static void avviaGioco() {
        System.out.println("====================================================");
        System.out.println("|                INIZIO AVVENTURA                 |");
        System.out.println("====================================================");
        char[][] mappa = {
                {'▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓'},
                {'▓',' ',' ',' ',' ',' ','▓',' ',' ',' ',' ',' ','▓',' ',' ',' ',' ',' ',' ','e','▓'},
                {'▓',' ',' ',' ',' ',' ','▓',' ',' ',' ',' ',' ','m',' ',' ',' ',' ',' ',' ',' ','▓'},
                {'▓',' ',' ',' ',' ',' ','▓','▓','▓','▓',' ','▓','▓',' ',' ',' ',' ',' ',' ',' ','▓'},
                {'▓',' ',' ',' ',' ',' ','▓','e',' ',' ',' ',' ','▓',' ',' ',' ',' ',' ',' ',' ','▓'},
                {'▓',' ',' ',' ',' ',' ','▓',' ',' ',' ',' ',' ','▓',' ',' ',' ',' ',' ',' ',' ','▓'},
                {'▓','▓','▓','m','▓','▓','▓',' ','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓',' ','▓'},
                {'▓','e',' ',' ','▓','e',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','▓',' ','▓'},
                {'▓',' ',' ',' ','▓',' ','▓','▓','▓','▓','▓','M','▓','▓','▓','▓','▓',' ','▓',' ','▓'},
                {'▓',' ',' ',' ','▓',' ','▓',' ',' ',' ',' ',' ',' ',' ',' ',' ','▓',' ','▓',' ','▓'},
                {'▓',' ',' ',' ','▓',' ','M',' ',' ',' ',' ','T',' ',' ',' ',' ','M',' ','▓',' ','▓'},
                {'▓',' ',' ',' ','▓',' ','▓',' ',' ',' ',' ',' ',' ',' ',' ',' ','▓',' ','▓',' ','▓'},
                {'▓',' ',' ',' ','▓',' ','▓','▓','▓','▓','▓','M','▓','▓','▓','▓','▓',' ','▓',' ','▓'},
                {'▓',' ',' ',' ','▓',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','▓',' ','▓'},
                {'▓','▓','▓','m','▓','▓','▓','▓','▓','▓','▓','U','▓','▓','▓','▓','▓','▓','▓','m','▓'},
                {'▓',' ',' ',' ',' ',' ',' ',' ',' ','e','▓','▓','▓',' ',' ',' ',' ',' ',' ',' ','▓'},
                {'▓',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','▓'},
                {'▓',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','▓'},
                {'▓',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','▓'},
                {'▓',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','▓'},
                {'▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓','▓'}
        };

        Caverna caverna = new Caverna(mappa);
        Prigioniero prigioniero = new Prigioniero(2, 3, 20, 5);                                                                                                                                        int mana = 6;
        GiocoConsole gioco = new GiocoConsole(caverna, prigioniero, mana);
        gioco.avvia();
    }

    public static void mostraComandi() {
        System.out.println("====================================================");
        System.out.println("|                    COMANDI                       |");
        System.out.println("====================================================");
        System.out.println("| W | Muovi su                                     |");
        System.out.println("| S | Muovi giù                                    |");
        System.out.println("| A | Muovi sinistra                               |");
        System.out.println("| D | Muovi destra                                 |");
        System.out.println("====================================================");
        System.out.println("|                  LEGENDA MAPPA                   |");
        System.out.println("====================================================");
        System.out.println("| ▓ | Muro                                         |");
        System.out.println("| m | Mostro piccolo                               |");
        System.out.println("| M | Mostro grande                                |");
        System.out.println("| e | Boost                                        |");
        System.out.println("| T | Tesoro                                       |");
        System.out.println("| U | Uscita della caverna                         |");
        System.out.println("| P | Prigioniero                                  |");
        System.out.println("====================================================");
        System.out.println("|                    ABILITÀ                       |");
        System.out.println("====================================================");
        System.out.println("| Fireball | Infligge molti danni                  |");
        System.out.println("====================================================");
    }
}