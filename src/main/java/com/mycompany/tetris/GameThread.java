/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

/**
 *
 * @author shtven
 */
public class GameThread extends Thread {
    private GamePanel panel;

    public GameThread(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(800);

                if (panel.puedeBajarActual()) {
                    panel.bajarTetromino();
                } else {
                    panel.fijarTetromino();
                    panel.eliminarLineasCompletas();
                    panel.generarNuevoBlock();
                }

                panel.repaint();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
