package gui;
import core.Videogioco;
import model.Caverna;
import model.Prigioniero;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GiocoGUI extends Videogioco {

    private static JFrame frame;
    private JPanel pannello;
    private JPanel sidebar;

    private int lastHP; //Queste due variabili servono per la logica di quando si usano le abilità o si prende danno
    private int lastMana;

    private ImageIcon heartIcon;
    private ImageIcon manaIcon;
    private ImageIcon wallIcon;
    private ImageIcon treasureIcon;
    private ImageIcon mostroPiccoloIcon;
    private ImageIcon mostroIcon;
    private ImageIcon playerIcon;
    private ImageIcon floorIcon;
    private ImageIcon exitIcon;
    private ImageIcon boostIcon;
    private ImageIcon volumeOn;
    private ImageIcon volumeOff;
    private ImageIcon bannerIcon;

    private boolean inPausa = false;
    private JPanel menuPausa;
    private boolean audioAttivo = true;  //per il pulsante che serve a mutare la musichetta

    public GiocoGUI(Caverna caverna, Prigioniero prigioniero) {
        super(caverna, prigioniero);

        GestoreAudio.playLoop("game.wav");

        // --- Caricamento Icone ---
        this.lastHP = prigioniero.getHealth();
        this.lastMana = prigioniero.getMana();
        this.heartIcon = caricaIcona("Utilities/textures/cuore.png");
        this.manaIcon = caricaIcona("Utilities/textures/pozione.png");
        this.wallIcon = caricaIcona("Utilities/textures/wall.png");
        this.treasureIcon = caricaIcona("Utilities/textures/treasure.png");
        this.mostroPiccoloIcon = caricaIcona("Utilities/textures/mostroPiccolo.png");
        this.mostroIcon = caricaIcona("Utilities/textures/mostro.png");
        this.playerIcon = caricaIcona("Utilities/textures/player.png");
        this.floorIcon = caricaIcona("Utilities/textures/floor.png");
        this.exitIcon = caricaIcona("Utilities/textures/exit.png");
        this.boostIcon = caricaIcona("Utilities/textures/boost.png");
        this.volumeOn = caricaIcona("Utilities/textures/audio_on.png");
        this.volumeOff = caricaIcona("Utilities/textures/audio_off.png");
        this.bannerIcon = caricaImmagine("Utilities/texturesMenu/horizzontalbanner.png");
        frame = new JFrame("Escape the Cave");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // --- Pannello Principale con Overlay di Pausa ---
        pannello = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                disegna(g);

                // Ora 'inPausa' non sarà più rosso
                if (inPausa) {
                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        //Layout null per posizionare il menu pausa esattamente al centro
        pannello.setLayout(null);
        pannello.setBackground(new Color(20, 20, 20));
        pannello.setFocusable(true);

        //MENU PAUSA

        menuPausa = new JPanel(new GridLayout(2, 1, 0, 25)); // 25 è lo spazio verticale tra i bottoni
        menuPausa.setOpaque(false); //rende invisibile il rettangolo grigio che c'e dietro i bottoni

        JButton btnRiprendi = creaBottone("RIPRENDI");
        JButton btnMenu = creaBottone("TORNA AL MENU");

        // Riprendi
        btnRiprendi.addActionListener(e -> gestisciPausa());

        //Torna al menu
        btnMenu.addActionListener(e -> {
            frame.dispose(); // Distrugge la finestra del gioco
            new MenuPrincipale();
        });

        menuPausa.add(btnRiprendi);
        menuPausa.add(btnMenu);

        menuPausa.setBounds(300, 250, 300, 150);
        menuPausa.setVisible(false);
        pannello.add(menuPausa);

        this.sidebar = creaSidebar(prigioniero);

        // --- KEY LISTENER ---
        pannello.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 1. Gestione Pausa (Sempre leggibile)
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gestisciPausa();
                }
                // 2. Se NON è ESC e NON siamo in pausa, allora legge i movimenti
                else if (!inPausa) {
                    char c = ' ';
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W: case KeyEvent.VK_UP:    c = 'W'; break;
                        case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  c = 'S'; break;
                        case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  c = 'A'; break;
                        case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: c = 'D'; break;
                        case KeyEvent.VK_E:                         c = 'E'; break;
                    }

                    // 3. Esegui il turno solo se il tasto premuto era valido
                    if (c != ' ') {
                        eseguiTurno(c);
                        aggiornaSidebar();
                        aggiornaPannello();

                        if (end) {
                            mostraRisultato();
                        }
                    }
                }
            }
        });

        int mappaLarghezza = Math.min(800/caverna.getColonne(), 600/caverna.getRighe()) * caverna.getColonne();  //Serve per far attaccare la sidebar alla mappa
        frame.add(sidebar, BorderLayout.WEST);
        frame.add(pannello, BorderLayout.CENTER);
        frame.setSize(250 + mappaLarghezza + 20, 800); // 250 è la larghezza sidebar
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        pannello.requestFocusInWindow();
    }

    //Metodo per gestire lo stato della pausa
    private void gestisciPausa() {
        inPausa = !inPausa; // Inverte lo stato true/false

        if (inPausa) {
            Videogioco.mettiInPausa();   //Ferma il timer mostri senno continuano ad andare mentre stai in pausa
            // Calcola il centro esatto rispetto al pannello
            int x = (pannello.getWidth() - menuPausa.getWidth()) / 2;
            int y = (pannello.getHeight() - menuPausa.getHeight()) / 2;
            menuPausa.setLocation(x, y);

            menuPausa.setVisible(true);   // Mostra i bottoni
        } else {
            Videogioco.riprendi();       //Fa ripartire il timer
            menuPausa.setVisible(false);  //Nasconde i bottoni
            pannello.requestFocusInWindow(); //torna a sentire i tasti
        }

        pannello.repaint(); // lo ridisegna
    }

    protected void aggiornaPannello() {
        if (pannello != null) {
            pannello.repaint();
        }
    }

    private JPanel creaSidebar(Prigioniero p) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Color bgSidebar = new Color(45, 45, 45);
        panel.setBackground(bgSidebar);

        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK));

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- SEZIONE VITA ---
        int numCuori = p.getHealth();
        if (numCuori > 0) {
            int righeCuori = (int) Math.ceil(numCuori / 2.0);     //Calcola quante righe servono per visualizzare i png, e arrotonda per eccesso con math ceil
            JPanel cuoriGrid = new JPanel(new GridLayout(righeCuori, 2, 5, 5));
            cuoriGrid.setBackground(bgSidebar);
            cuoriGrid.setMaximumSize(new Dimension(80, righeCuori * 35)); //limito il Jpanel

            for (int i = 0; i < numCuori; i++) {
                // Usiamo heartIcon caricata nel costruttore
                JLabel l;
                if (heartIcon != null) {
                    l = new JLabel(heartIcon); // Se l'immagine esiste, usa l'icona
                }else {
                    l = new JLabel("●");
                }
                if (heartIcon == null) l.setForeground(Color.RED);

                l.setPreferredSize(new Dimension(32, 32));
                l.setOpaque(false); //Trasparente così si vede lo sfondo della sidebar
                cuoriGrid.add(l);
            }
            panel.add(cuoriGrid);
        }

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- SEZIONE MANA ---
        int numMana = p.getMana();
        if (numMana > 0) {
            int righeMana = (int) Math.ceil(numMana / 2.0);
            JPanel manaGrid = new JPanel(new GridLayout(righeMana, 2, 5, 5));
            manaGrid.setBackground(bgSidebar);
            manaGrid.setMaximumSize(new Dimension(80, righeMana * 35));

            for (int i = 0; i < numMana; i++) {
                // Usiamo manaIcon caricata nel costruttore
                JLabel l;
                if (manaIcon != null) {
                    l = new JLabel(manaIcon);
                }else {
                    l = new JLabel("○");
                }
                if (manaIcon == null) l.setForeground(Color.CYAN);

                l.setPreferredSize(new Dimension(32, 32));
                l.setOpaque(false);
                manaGrid.add(l);
            }
            panel.add(manaGrid);
        }
        panel.add(Box.createVerticalGlue()); //Spinge tutto quello che c'è sopra verso l'alto e quello che c'è sotto verso il basso

        // --- PULSANTE AUDIO ---
        JButton btnMuto = new JButton(volumeOn); //imposta l'icona iniziale
        btnMuto.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMuto.setMaximumSize(new Dimension(50, 50));
        btnMuto.setFocusable(false);

        //  rende il bottone invisibile lasciando solo l'icona
        btnMuto.setContentAreaFilled(false);
        btnMuto.setBorderPainted(false);

        btnMuto.addActionListener(e -> {
            audioAttivo = !audioAttivo;
            if (audioAttivo) {
                btnMuto.setIcon(volumeOn);
                GestoreAudio.playLoop("game.wav");
            } else {
                btnMuto.setIcon(volumeOff);
                GestoreAudio.stop();
            }
        });

        panel.add(btnMuto);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Spazio dal bordo inferiore

        panel.revalidate();
        panel.repaint();
        return panel;
    }

    private ImageIcon caricaIcona(String nomeFile) {
        try {
            java.net.URL imgURL = getClass().getResource("/" + nomeFile);
            if (imgURL != null) {
                java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(imgURL);
                if (img != null) {
                    return new ImageIcon(img.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH));
                }
            }
        } catch (Exception e) {
            System.out.println("Errore lettura file: " + nomeFile);
        }
        return null;
    }

    private ImageIcon caricaImmagine(String nomeFile) {
        try {
            java.net.URL imgURL = getClass().getResource("/" + nomeFile);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            }
        } catch (Exception e) {
            System.out.println("Errore lettura file: " + nomeFile);
        }
        return null;
    }

    public void aggiornaSidebar() { //Fa in modo che quando si prende danno o si usa l'abilità viene aggiornato il numero di png
        if (prigioniero.getHealth() != lastHP || prigioniero.getMana() != lastMana) {
            frame.remove(sidebar);
            sidebar = creaSidebar(prigioniero);
            frame.add(sidebar, BorderLayout.WEST);

            lastHP = prigioniero.getHealth();
            lastMana = prigioniero.getMana();

            frame.revalidate();
            frame.repaint();
        }
    }

    private void disegna(Graphics g) {
        int rows = caverna.getRighe();
        int cols = caverna.getColonne();
        int cellWidth = pannello.getWidth() / cols;
        int cellHeight = pannello.getHeight() / rows;
        int cellSize = Math.min(cellWidth, cellHeight);

        int offsetX = 0;
        int offsetY = (pannello.getHeight() - (rows * cellSize)) / 2;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = offsetX + c * cellSize;
                int y = offsetY + r * cellSize;
                char cell = caverna.getCella(r, c);

                if (cell != '#') {
                    if (floorIcon != null) {
                        g.drawImage(floorIcon.getImage(), x, y, cellSize, cellSize, null);
                    } else {
                        g.setColor(new Color(30, 30, 30));
                        g.fillRect(x, y, cellSize, cellSize);
                    }
                }

                if (cell == '#') {
                    if (wallIcon != null) {
                        g.drawImage(wallIcon.getImage(), x, y, cellSize, cellSize, null);
                    } else {
                        g.setColor(Color.DARK_GRAY);
                        g.fillRect(x, y, cellSize, cellSize);
                    }
                } else {
                    switch (cell) {
                        case 'U':
                            if (exitIcon != null) g.drawImage(exitIcon.getImage(), x, y, cellSize, cellSize, null);
                            else { g.setColor(Color.GREEN); g.fillRect(x, y, cellSize, cellSize); }
                            break;
                        case 'e':
                            if (boostIcon != null) g.drawImage(boostIcon.getImage(), x, y, cellSize, cellSize, null);
                            else { g.setColor(Color.CYAN); g.fillRect(x, y, cellSize, cellSize); }
                            break;
                        case 'T':
                            if (treasureIcon != null) g.drawImage(treasureIcon.getImage(), x, y, cellSize, cellSize, null);
                            else { g.setColor(Color.YELLOW); g.fillRect(x, y, cellSize, cellSize); }
                            break;
                        case 'm':
                            if (mostroPiccoloIcon != null) g.drawImage(mostroPiccoloIcon.getImage(), x, y, cellSize, cellSize, null);
                            else { g.setColor(Color.PINK); g.fillRect(x, y, cellSize, cellSize); }
                            break;
                        case 'M':
                            if (mostroIcon != null) g.drawImage(mostroIcon.getImage(), x, y, cellSize, cellSize, null);
                            else { g.setColor(Color.MAGENTA); g.fillRect(x, y, cellSize, cellSize); }
                            break;
                    }
                }
            }
        }

        // player pos
        int playerX = offsetX + prigioniero.getY() * cellSize;
        int playerY = offsetY + prigioniero.getX() * cellSize;

        if (playerIcon != null) {
            g.drawImage(playerIcon.getImage(), playerX + 2, playerY + 2, cellSize - 4, cellSize - 4, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(playerX + 4, playerY + 4, cellSize - 8, cellSize - 8);
        }

        if (bannerIcon != null) {
            int mapWidth = cols * cellSize;
            int startX = offsetX + mapWidth;
            int availableWidth = pannello.getWidth() - startX;
            if (availableWidth > 0) {
                g.drawImage(bannerIcon.getImage(), startX, 0, availableWidth, pannello.getHeight(), null);
            }
        }
    }

    public void avvia() {
        pannello.requestFocusInWindow();
    }

    // popup risultato
    protected void mostraRisultato() {
        String msg;
        if (WL) {
            msg = "Bella partita! Hai VINTO :D !!";
        } else {
            msg = "Noooo! Hai Perso! :C";
        }
        JOptionPane.showMessageDialog(frame, msg);
        Videogioco.mettiInPausa();
        frame.dispose();
        new MenuPrincipale();
    }

    public static void informazioneAbilità(){
        JOptionPane.showMessageDialog(frame, "Non hai ancora sbloccato l'abilità!");
    }

    public static void informazioneMuro(){
        JOptionPane.showMessageDialog(frame, "Hai sbloccato l'abilità Piazza Muro!");
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
}