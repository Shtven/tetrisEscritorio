package com.mycompany.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {

    private final int Filas = 20;
    private final int Columnas = 10;
    private final int TamañoBlock = 30;
    private Color[][] red;
    private ScoreFile scoreFile;
    private Tetromino tetromino;
    private GameThread gameThread;
    private JTable scoreTable;
    private ClienteTetris cliente;

    public GamePanel(ClienteTetris cliente, ScoreFile scoreFile, JTable scoreTable) {
        red = new Color[Filas][Columnas];
        setFocusable(true);
        requestFocusInWindow();
        this.cliente = cliente;
        this.scoreFile = scoreFile;
        this.scoreTable = scoreTable;
        generarNuevoBlock();

        setPreferredSize(new Dimension(Columnas * TamañoBlock, Filas * TamañoBlock));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (tetromino == null) return;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        tetromino.rotar(red);
                        SoundPlayer.playSound("rotate.wav");
                        break;
                    case KeyEvent.VK_LEFT:
                        tetromino.mover(-1, red);
                        SoundPlayer.playSound("move.wav");
                        break;
                    case KeyEvent.VK_RIGHT:
                        tetromino.mover(1, red);
                        SoundPlayer.playSound("move.wav");
                        break;
                    case KeyEvent.VK_DOWN:
                        if (puedeBajarActual()) {
                            tetromino.bajar();
                            SoundPlayer.playSound("drop.wav");
                        } else {
                            fijarTetromino();
                            eliminarLineasCompletas();
                            generarNuevoBlock();
                        }
                        break;
                }

                repaint();
            }
        });
    }

    public void iniciar() {
        gameThread = new GameThread(this);
        gameThread.start();
        scoreFile.setScore(0);
    }

    public void generarNuevoBlock() {
        int tipo = (int) (Math.random() * 5);
        int columnas = Columnas / 2 - 1;
        Tetromino nuevo = new Tetromino(tipo, columnas);

        for (Block b : nuevo.getBlocks()) {
            int fila = b.getFila();
            int col = b.getColumna();
            if (fila >= 0 && fila < Filas && col >= 0 && col < Columnas && red[fila][col] != null) {

                if (cliente != null) {
                    cliente.notificarFinJuego();
                }

                try { Thread.sleep(100); } catch (InterruptedException ex) {}

                String ganador = "Desconocido";
                if (scoreTable.getRowCount() > 0) {
                    Object user = scoreTable.getValueAt(0, 0);
                    Object score = scoreTable.getValueAt(0, 1);
                    if (user != null && score != null) {
                        ganador = "Ganador: " + user.toString();
                    }
                }

                int opcion = JOptionPane.showOptionDialog(
                        this,
                        "Fin del juego\n\n" + ganador + "\n¿Quieres jugar otra vez?",
                        "Game Over",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Jugar de nuevo", "Salir"},
                        "Jugar de nuevo"
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    reiniciarJuego();
                } else {
                    System.exit(0);
                }

                return;
            }
        }

        this.tetromino = nuevo;
        repaint();
    }

    public void reiniciarJuego() {
        red = new Color[Filas][Columnas];
        scoreFile.setScore(0);
        cliente.enviarPuntaje(0);
        generarNuevoBlock();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(200, 200, 200, 60));
        for (int fila = 0; fila < Filas; fila++) {
            for (int col = 0; col < Columnas; col++) {
                g.drawRect(col * TamañoBlock, fila * TamañoBlock, TamañoBlock, TamañoBlock);
            }
        }

        for (int fila = 0; fila < Filas; fila++) {
            for (int col = 0; col < Columnas; col++) {
                if (red[fila][col] != null) {
                    g.setColor(red[fila][col]);
                    g.fillRect(col * TamañoBlock, fila * TamañoBlock, TamañoBlock, TamañoBlock);
                    g.setColor(Color.BLACK);
                    g.drawRect(col * TamañoBlock, fila * TamañoBlock, TamañoBlock, TamañoBlock);
                }
            }
        }

        if (tetromino != null) {
            tetromino.draw(g, TamañoBlock);
        }
        for (Block b : tetromino.getBlocks()) {
            int fila = b.getFila();
            int col = b.getColumna();
            if (fila >= Filas) {
                System.out.println("⚠️ Bloque fuera de límites: fila=" + fila + ", col=" + col);
            }
        }
    }

    public boolean puedeBajarActual() {
        for (Block b : tetromino.getBlocks()) {
            int fila = b.getFila() + 1;
            int col = b.getColumna();
            if (fila >= Filas || red[fila][col] != null) {
                return false;
            }
        }
        return true;
    }

    public void bajarTetromino() {
        tetromino.bajar();
    }

    public void fijarTetromino() {
        for (Block b : tetromino.getBlocks()) {
            int fila = b.getFila();
            int col = b.getColumna();

            if (fila >= 0 && fila < Filas && col >= 0 && col < Columnas) {
                red[fila][col] = tetromino.getColor();
            }
        }
    }

    public void eliminarLineasCompletas() {
        for (int fila = Filas - 1; fila >= 0; fila--) {
            boolean lineaCompleta = true;

            for (int col = 0; col < Columnas; col++) {
                if (red[fila][col] == null) {
                    lineaCompleta = false;
                    break;
                }
            }

            if (lineaCompleta) {
                for (int i = fila; i > 0; i--) {
                    for (int j = 0; j < Columnas; j++) {
                        red[i][j] = red[i - 1][j];
                    }
                }

                for (int j = 0; j < Columnas; j++) {
                    red[0][j] = null;
                }

                fila++;
                scoreFile.setScore(scoreFile.getScore() + 100);
                SoundPlayer.playSound("line.wav");
                if (cliente != null) {
                    cliente.enviarPuntaje(scoreFile.getScore());
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Columnas * TamañoBlock, Filas * TamañoBlock);
    }
}
    