/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tetris;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;

/**
 *
 * @author shtven
 */
public class Tetris {
    
        public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Básico");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setSize(300, 720);
        frame.setResizable(false);
        
        //GamePanel panel = new GamePanel();
        //frame.add(panel);
        
        frame.setVisible(true);

        //panel.iniciar();
    }

}

