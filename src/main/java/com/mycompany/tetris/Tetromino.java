/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

/**
 *
 * @author shtven
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tetromino {
    private List<Block> blocks = new ArrayList<>();
    private Color color;

    public Tetromino(int type, int startCol) {
        color = new Color((int)(Math.random() * 0xFFFFFF));

        switch (type) {
            case 0: 
                Block cuadrado = new BlockBasico();
                blocks = cuadrado.generar(startCol, color);
                break;
            case 1:
                Block enI = new BlockI();
                blocks = enI.generar(startCol, color);
                break;
            case 2:
                Block enL = new BlockL();
                blocks = enL.generar(startCol, color);
                break;
            case 3:
                Block enZ = new BlockZ();
                blocks = enZ.generar(startCol, color);
                break;
            case 4:
                Block enT = new BlockT();
                blocks = enT.generar(startCol, color);
                break;
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void bajar() {
        for (Block b : blocks) b.mover();
    }

    public void mover(int dx) {
        for (Block b : blocks) b.setColumna(b.getColumna() + dx);
    }

    public Color getColor() {
        return color;
    }
}
