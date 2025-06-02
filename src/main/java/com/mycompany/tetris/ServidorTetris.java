package com.mycompany.tetris;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServidorTetris {
    private static final int PUERTO = 1234;
    private static Map<String, Integer> puntajes = new ConcurrentHashMap<>();
    private static List<PrintWriter> clientes = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Servidor Tetris iniciado en el puerto " + PUERTO);
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ManejadorCliente(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ManejadorCliente implements Runnable {
        private Socket socket;
        private BufferedReader entrada;
        private PrintWriter salida;
        private String usuario = "";

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                salida = new PrintWriter(socket.getOutputStream(), true);

                synchronized (clientes) {
                    clientes.add(salida);
                }

                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    if (mensaje.startsWith("Se unió:")) {
                        usuario = mensaje.substring(8);
                        puntajes.put(usuario, 0);
                        enviarPuntajes();
                    } else if (mensaje.startsWith("Puntaje:")) {
                        String[] partes = mensaje.split(":");
                        if (partes.length == 3) {
                            String nombre = partes[1];
                            int score = Integer.parseInt(partes[2]);
                            puntajes.put(nombre, score);
                            enviarPuntajes();
                        }
                    } else if (mensaje.startsWith("Perdió:")) {
                        String nombre = mensaje.substring(7);
                        enviarATodos(nombre + " perdió");
                        
                        enviarATodos("Puntajes finales:");
                        puntajes.entrySet().stream()
                                .sorted((a, b) -> b.getValue() - a.getValue())
                                .forEach(entry -> enviarATodos(" - " + entry.getKey() + ": " + entry.getValue() + " pts"));
                    }else if(mensaje.startsWith("Chat:")){
                        enviarATodos(mensaje);
                    }
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado: " + usuario);
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}

                synchronized (clientes) {
                    clientes.remove(salida);
                }
            }
        }

        private void enviarPuntajes() {
            StringBuilder sb = new StringBuilder("Puntajes\n");
            puntajes.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n"));

            System.out.println("→ Enviado: " + sb);

            synchronized (clientes) {
                for (PrintWriter out : clientes) {
                    for (String linea : sb.toString().split("\n")) {
                        out.println(linea);
                    }
                    out.println();
                }
            }
        }


        private static void enviarATodos(String mensaje) {
            synchronized (clientes) {
                for (PrintWriter out : clientes) {
                    out.println(mensaje);
                }

            }
        }

    }
}