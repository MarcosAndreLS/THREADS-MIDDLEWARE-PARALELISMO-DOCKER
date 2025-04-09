package cliente;

import javax.swing.*;
import java.awt.*;

public class Screen extends JFrame {
    public Screen(int letras, int numeros) {
        setTitle("Análise de Texto");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        JLabel lblLetras = new JLabel("Total de letras: " + letras);
        JLabel lblNumeros = new JLabel("Total de números: " + numeros);

        lblLetras.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNumeros.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.add(lblLetras);
        panel.add(lblNumeros);

        add(panel);
    }
}
