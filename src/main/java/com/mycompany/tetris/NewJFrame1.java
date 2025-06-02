package com.mycompany.tetris;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NewJFrame1 extends JFrame {

    private ClienteTetris cliente;
    private JPanel panelTetris;
    private JTable scoreTable;
    private JTextArea chat;
    private JTextField mensajeAlChat;
    private JButton enviarAlChat;

    public NewJFrame1() {
        setTitle("Tetris Multijugador");
        setSize(1000, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();

        ScoreFile file = new ScoreFile();
        if (file.getUser() == null) {
            file.setUser(JOptionPane.showInputDialog(null, "Ingresa tu nombre de usuario:"));
        }

        try {
            cliente = new ClienteTetris("localhost", scoreTable, file.getUser(), chat);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar al servidor.\n¿Ejecutaste el ServidorTetris?",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        GamePanel panel = new GamePanel(cliente, file, scoreTable);
        mostrarPanel(panel);
        panel.iniciar();
    }

    private void initComponents() {
        // Panel de juego
        panelTetris = new JPanel(new BorderLayout());
        panelTetris.setPreferredSize(new Dimension(350, 680));
        panelTetris.setBackground(Color.BLACK);

        // Tabla de puntuaciones
        scoreTable = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"Jugador", "Puntaje"}));
        scoreTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        scoreTable.setRowHeight(24);
        scoreTable.setEnabled(false);
        JScrollPane scrollScore = new JScrollPane(scoreTable);
        scrollScore.setBorder(BorderFactory.createTitledBorder("Puntajes"));

        // Área de chat
        chat = new JTextArea();
        chat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chat.setEditable(false);
        chat.setLineWrap(true);
        chat.setWrapStyleWord(true);
        chat.setBackground(new Color(245, 245, 245));
        chat.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollChat = new JScrollPane(chat);
        scrollChat.setBorder(BorderFactory.createTitledBorder("Chat Global"));

        // Input de mensaje y botón
        mensajeAlChat = new JTextField();
        mensajeAlChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mensajeAlChat.setPreferredSize(new Dimension(250, 35));
        mensajeAlChat.addActionListener(e -> enviarMensaje());

        enviarAlChat = new JButton("Enviar");
        enviarAlChat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        enviarAlChat.setBackground(new Color(0, 123, 255));
        enviarAlChat.setForeground(Color.WHITE);
        enviarAlChat.setFocusPainted(false);
        enviarAlChat.addActionListener(e -> enviarMensaje());

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.add(mensajeAlChat, BorderLayout.CENTER);
        inputPanel.add(enviarAlChat, BorderLayout.EAST);

        // Panel derecho
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(10, 10));
        rightPanel.setPreferredSize(new Dimension(600, 700));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(new Color(30, 30, 30));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(rightPanel.getBackground());
        container.add(scrollScore);
        container.add(Box.createVerticalStrut(10));
        container.add(scrollChat);
        container.add(Box.createVerticalStrut(10));
        container.add(inputPanel);

        rightPanel.add(container, BorderLayout.CENTER);

        // Split horizontal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelTetris, rightPanel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.3);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
    }

    private void enviarMensaje() {
        String texto = mensajeAlChat.getText().trim();
        if (!texto.isEmpty()) {
            cliente.enviarMensajeChat(texto);
            mensajeAlChat.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Ingresa un mensaje para enviar.");
        }
    }

    private void mostrarPanel(JPanel panel) {
        panel.setSize(350, 680);
        panelTetris.removeAll();
        panelTetris.add(panel, BorderLayout.CENTER);
        panelTetris.revalidate();
        panelTetris.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {}
            new NewJFrame().setVisible(true);
        });
    }
}
