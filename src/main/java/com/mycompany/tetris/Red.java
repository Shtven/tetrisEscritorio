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

public class Red {
    private Color[][] matrix;
    private int filas, columnas;

    public Red(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        matrix = new Color[filas][columnas];
    }

    public void setColor(int filas, int columnas, Color color) {
        if (filas >= 0 && filas < this.filas && columnas >= 0 && columnas < this.columnas)
        matrix[filas][columnas] = color;
    }

    public Color getColor(int filas, int columnas) {
        if (filas >= 0 && filas < this.filas && columnas >= 0 && columnas < this.columnas)
            return matrix[filas][columnas];
        return null;
    }
}

