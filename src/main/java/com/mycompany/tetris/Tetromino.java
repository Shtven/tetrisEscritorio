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

    public void mover(int dx, Color[][] red) {
        for (Block b : blocks) {
            int nuevaCol = b.getColumna() + dx;
            int fila = b.getFila();

            if (nuevaCol < 0 || nuevaCol >= red[0].length || red[fila][nuevaCol] != null) {
                return;
            }
        }
        for (Block b : blocks) b.setColumna(b.getColumna() + dx);
    }

    public Color getColor() {
        return color;
    }

    public void rotar(Color[][] red) {
        Block pivote = blocks.get(0);
        int pivotRow = pivote.getFila();
        int pivotCol = pivote.getColumna();

        List<Block> nuevaPosicion = new ArrayList<>();

        for (Block b : blocks) {
            int fila = b.getFila();
            int col = b.getColumna();

            int nuevaFila = pivotRow - (col - pivotCol);
            int nuevaCol = pivotCol + (fila - pivotRow);

            if (nuevaFila < 0 || nuevaFila >= red.length || nuevaCol < 0 || nuevaCol >= red[0].length)
                return;

            if (red[nuevaFila][nuevaCol] != null)
                return;

            nuevaPosicion.add(b.cloneConPosicion(nuevaFila, nuevaCol));
        }

        blocks = nuevaPosicion;
    }


    public void draw(Graphics g, int tamaño) {
        for (Block b : blocks) {
            g.setColor(color);
            g.fillRect(b.getColumna() * tamaño, b.getFila() * tamaño, tamaño, tamaño);
            g.setColor(Color.BLACK);
            g.drawRect(b.getColumna() * tamaño, b.getFila() * tamaño, tamaño, tamaño);
        }
    }



}
