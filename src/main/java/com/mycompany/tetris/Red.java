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
    private int rows, cols;

    public Red(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        matrix = new Color[rows][cols];
    }

    public void setColor(int row, int col, Color color) {
        if (row >= 0 && row < rows && col >= 0 && col < cols)
            matrix[row][col] = color;
    }

    public Color getColor(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols)
            return matrix[row][col];
        return null;
    }
}

