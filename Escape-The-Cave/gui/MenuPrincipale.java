package gui;
import model.Caverna;
import model.Prigioniero;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class MenuPrincipale {

    // finestra principale del menu
    private JFrame frame;

    // mappa che viene usata di default
    private File mappaSelezionata = new File("Utilities/mappe/mappa1.txt");

    public MenuPrincipale() {

        // musica del menu
        GestoreAudio.playLoop("menu.wav");

        // crea la finestra
        frame = new JFrame("Escape the Cave - Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLocationRelativeTo(null);

        // pannello con sfondo
        PannelloSfondo panel = new PannelloSfondo("Utilities/texturesMenu/cave.png");

        // layout a griglia
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // titolo
        ImageIcon iconaOriginale = new ImageIcon("Utilities/texturesMenu/titolo.png");
        Image img = iconaOriginale.getImage();
        Image imgScalata = img.getScaledInstance(500, -1, Image.SCALE_SMOOTH);
        JLabel logoTitolo = new JLabel(new ImageIcon(imgScalata));
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(logoTitolo, gbc);

        // bottoni
        JButton btnGioca = creaBottone("GIOCA");
        JButton btnCarica = creaBottone("CARICA");
        JButton btnEsci = creaBottone("ESCI");

        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 1;
        panel.add(btnGioca, gbc);
        gbc.gridy = 2;
        panel.add(btnCarica, gbc);
        gbc.gridy = 3;
        panel.add(btnEsci, gbc);

        //gioca
        btnGioca.addActionListener(e -> {
            if (mappaSelezionata.exists()) {
                avviaGioco();
            } else {
                JOptionPane.showMessageDialog(frame, "Errore il file mappa1.txt non è stato trovato");
            }
        });

        //carica
        btnCarica.addActionListener(e -> {

            File cartellaMappe = new File("Utilities/mappe");

            if (!cartellaMappe.exists()) cartellaMappe.mkdir();

            // finestra per scegliere file
            JFileChooser fileChooser = new JFileChooser(cartellaMappe);
            fileChooser.setDialogTitle("Seleziona la mappa");
            fileChooser.setFileFilter(new FileNameExtensionFilter("File txt", "txt"));
            fileChooser.setAcceptAllFileFilterUsed(false);

            // se scegli un file
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                mappaSelezionata = fileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(frame, "Mappa caricata: " + mappaSelezionata.getName());
            }
        });

        //esci
        btnEsci.addActionListener(e -> System.exit(0));

        frame.add(panel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    // crea bottone
    private JButton creaBottone(String testo) {
        JButton b = new JButton(testo);

        b.setFont(new Font("Arial", Font.BOLD, 18));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setBorderPainted(true);
        b.setPreferredSize(new Dimension(250, 60));

        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(255, 165, 0));
                b.setOpaque(true);
            }

            public void mouseExited(MouseEvent e) {
                b.setOpaque(false);
            }
        });

        return b;
    }

    private void avviaGioco() {
        frame.dispose();

        char[][] matrice = CaricaMappa.caricaMatriceDaFile(mappaSelezionata.getAbsolutePath());

        if (matrice != null) {
            Caverna caverna = new Caverna(matrice);
            Prigioniero prigioniero = new Prigioniero(1, 1, 2, 0);
            GiocoGUI gioco = new GiocoGUI(caverna, prigioniero);
            gioco.avvia();
        } else {
            JOptionPane.showMessageDialog(null, "Errore nel caricamento della mappa");
        }
    }
}