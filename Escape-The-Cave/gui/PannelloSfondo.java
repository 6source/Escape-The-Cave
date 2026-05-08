package gui;
import javax.swing.*;
import java.awt.*;

public class PannelloSfondo extends JPanel {

    private Image immagine;

    public PannelloSfondo(String percorsoImmagine) {
        java.net.URL imgURL = getClass().getResource("/" + percorsoImmagine);

        if (imgURL != null) {
            this.immagine = new ImageIcon(imgURL).getImage();
        } else {
            System.out.println("Immagine non trovata");
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (immagine != null) {
            g.drawImage(immagine, 0, 0, getWidth(), getHeight(), this);
        }
    }
}