package com.mycompany.tetris;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClienteTetris {
    private Socket soket;
    private BufferedReader entradaDatos;
    private PrintWriter salidaDatos;
    private JTable scoreTable;
    private String usuario;

    private List<String[]> puntajes = new ArrayList<>();
    private boolean recibiendoPuntajes = false;
    private StringBuilder puntajesBuilder = new StringBuilder();

    public ClienteTetris(String direccion, JTable scoreTable, String usuario) {
        this.scoreTable = scoreTable;
        this.usuario = usuario;

        try{

            soket = new Socket(direccion, 1234);
            entradaDatos = new BufferedReader(new InputStreamReader(soket.getInputStream()));
            salidaDatos = new PrintWriter(soket.getOutputStream(), true);

            salidaDatos.println("Se unió:" + usuario);

            new Thread( () -> {
                String linea;
                try {
                    while ((linea = entradaDatos.readLine()) != null) {
                        procesarMensaje(linea);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void procesarMensaje(String mensaje) {
        if (mensaje.startsWith("Puntajes")) {
            recibiendoPuntajes = true;
            puntajesBuilder.setLength(0);
            puntajes.clear();
        } else if (recibiendoPuntajes) {
            if (mensaje.trim().isEmpty()) {
                recibiendoPuntajes = false;
                SwingUtilities.invokeLater(this::actualizarTabla);
            } else {
                try {
                    String[] partes = mensaje.split(":");
                    if (partes.length == 2) {
                        String user = partes[0].trim();
                        String score = partes[1].trim();
                        puntajes.add(new String[]{user, score});
                        System.out.println("→ Recibido del servidor: " + mensaje);
                    }
                } catch (Exception e) {
                    System.out.println("Error al procesar mensaje: " + mensaje);
                }
            }
        }
    }

    public void enviarPuntaje(int puntaje) {
        salidaDatos.println("Puntaje:" + usuario + ":" + puntaje);
    }

    public void notificarFinJuego() {
        salidaDatos.println("Perdió:" + usuario);
    }

    public void enviarMensajeChat(String texto) {
        salidaDatos.println("CHAT:" + usuario + ":" + texto);
    }

    private void actualizarTabla() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Usuario", "Puntaje"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (String[] fila : puntajes) {
            model.addRow(fila);
        }

        scoreTable.setModel(model);
    }

}
