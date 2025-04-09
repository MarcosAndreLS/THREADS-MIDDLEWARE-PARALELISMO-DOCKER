package cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Screen extends JFrame {
    public Screen(int letras, int numeros) {
        setTitle("Resultado da Análise");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Análise de Texto");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLetras = new JLabel("Total de letras: " + letras);
        lblLetras.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblLetras.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNumeros = new JLabel("Total de números: " + numeros);
        lblNumeros.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblNumeros.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnSalvar = new JButton("Salvar resultado");
        btnSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalvar.addActionListener((ActionEvent e) -> salvarResultado(letras, numeros));

        JButton btnNovo = new JButton("Analisar outro arquivo");
        btnNovo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNovo.addActionListener((ActionEvent e) -> {
            dispose(); // Fecha a janela atual
            Main.main(new String[0]); // Reabre o processo
        });

        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lblLetras);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblNumeros);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnSalvar);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnNovo);

        add(panel);
    }

    private void salvarResultado(int letras, int numeros) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar como");
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            Path path = fileChooser.getSelectedFile().toPath();
            String conteudo = "Total de letras: " + letras + "\nTotal de números: " + numeros;

            try {
                Files.writeString(path, conteudo);
                JOptionPane.showMessageDialog(this, "Resultado salvo com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar o arquivo: " + ex.getMessage());
            }
        }
    }
}
