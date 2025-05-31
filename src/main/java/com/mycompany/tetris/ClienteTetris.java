package com.mycompany.tetris;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteTetris {
    private Socket soket;
    private BufferedReader entradaDatos;
    private PrintWriter salidaDatos;
    private JTextArea scoreText;
    private String usuario;

    public ClienteTetris(String direccion, JTextArea scoreText, String usuario) {
        this.scoreText = scoreText;
        this.usuario = usuario;

        try{

            soket = new Socket(direccion, 1234);
            entradaDatos = new BufferedReader(new InputStreamReader(soket.getInputStream()));
            salidaDatos = new PrintWriter(soket.getOutputStream());

            salidaDatos.println("Se unió:" + usuario);

            new Thread( () -> {
                String linea;
                try {
                    while ((linea = entradaDatos.readLine()) != null) {
                        procesarMensaje(linea);
                    }
                } catch (Exception e) {
                    mostrarMensaje("Sin conexion con el servidor");
                }
            }).start();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void procesarMensaje(String mensaje) {
        if (scoreText != null) {
            SwingUtilities.invokeLater(() -> scoreText.append(mensaje + "\n"));
        }
    }

    private void mostrarMensaje(String mensaje) {
        if (scoreText != null) {
            SwingUtilities.invokeLater(() -> scoreText.append(mensaje + "\n"));
        }
    }

    public void enviarPuntaje(int puntaje) {
        salidaDatos.println("Puntaje:" + usuario + ":" + puntaje);
    }

    public void notificarFinJuego() {
        salidaDatos.println("Perdió:" + usuario);
    }
}
