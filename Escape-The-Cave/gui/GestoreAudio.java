package gui;
import javax.sound.sampled.*;
import java.io.File;

public class GestoreAudio {
    private static Clip clip;

    public static void playLoop(String nomeFile) {
        // Ferma la musica precedente se è in esecuzione
        stop();
        try {
            File fileAudio = new File("Utilities/songs/" + nomeFile);

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(fileAudio); //si occupa di decodificare la canzone
            clip = AudioSystem.getClip(); //richiede spazio in memoria per poter mettere la canzone
            clip.open(audioStream); //prende i dati della canzone

            // Loop infinito
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}