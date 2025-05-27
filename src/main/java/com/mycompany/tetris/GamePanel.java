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

    private final int Filas = 20;
    private final int Columnas = 10;
    private final int TamañoBlock = 30;
    private Color[][] red;
    private Tetromino tetromino;
    private GameThread gameThread;

    public GamePanel() {
        red = new Color[Filas][Columnas];
        setFocusable(true);
        generarNuevoBlock();
    }

    public void iniciar() {
        gameThread = new GameThread(this);
        gameThread.start();
    }

    public void generarNuevoBlock() {
        int tipo = (int) (Math.random() * 5);
        int columnas = Columnas / 2 - 1;
        tetromino = new Tetromino(tipo, columnas);
        repaint();
    }

}
