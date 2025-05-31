/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tetris;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shtven
 */
public class BlockI implements Block{
        private int fila;
    private int columna;
    private Color color;
    
    public BlockI(){
        
    }
    
    private  BlockI(int fila, int columna, Color color) {
        this.fila = fila; 
        this.columna = columna;
        this.color = color;
    }
    
    @Override
    public List<Block> generar(int columna, Color color){
        List<Block> blocks = new ArrayList<>();
        
        for (int i = 0; i < 4; i++) {
            blocks.add(new BlockI(i, columna, color));
        }
        
        return blocks;
    }
    
    @Override
    public void mover() {
        fila++;
    }

    @Override
    public int getFila() {
        return fila;
    }

    @Override
    public int getColumna() {
        return columna;
    }

    @Override
    public Color getColor() {
        return color;
    }
    
    @Override
    public void setColumna(int columna) {
        this.columna = columna;
    }

    @Override
    public Block cloneConPosicion(int fila, int columna) {
        return new BlockI(fila, columna, this.color);
    }
}
