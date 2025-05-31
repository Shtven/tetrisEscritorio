/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

/**
 *
 * @author shtven
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {

    private final int Filas = 23;
    private final int Columnas = 10;
    private final int TamañoBlock = 30;
    private Color[][] red;
    private ScoreFile scoreFile;
    private Tetromino tetromino;
    private GameThread gameThread;
    private JTextArea textScore;
    private ClienteTetris cliente;

    public GamePanel(ClienteTetris cliente, ScoreFile scoreFile, JTextArea textScore) {
        red = new Color[Filas][Columnas];
        setFocusable(true);
        this.cliente = cliente;
        this.scoreFile = scoreFile;
        this.textScore = textScore;
        generarNuevoBlock();


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (tetromino == null) return;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        tetromino.rotar(red);
                        break;
                    case KeyEvent.VK_LEFT:
                        tetromino.mover(-1, red);
                        break;
                    case KeyEvent.VK_RIGHT:
                        tetromino.mover(1, red);
                        break;
                    case KeyEvent.VK_DOWN:
                        if (puedeBajarActual()) {
                            tetromino.bajar();
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

                try { Thread.sleep(1000); } catch (InterruptedException ex) {}

                int opcion = JOptionPane.showOptionDialog(
                        this,
                        "Fin del juego\n\n" + "\n¿Quieres jugar otra vez?",
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
        if (textScore != null) {
            textScore.setText("usuario: " + scoreFile.getUser() + "                 Score: " + String.valueOf(scoreFile.getScore()));
        }
        generarNuevoBlock();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

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

    public void bajarTetromino(){
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

                if(textScore != null){
                    textScore.setText("usuario: " + scoreFile.getUser() + "                 Score: " + String.valueOf(scoreFile.getScore()));
                    cliente.enviarPuntaje(scoreFile.getScore());
                }
            }
        }
    }



}
