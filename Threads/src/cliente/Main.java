package cliente;

import javax.swing.*;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("D:\\Faculdade\\Sistemas_Distribuidos\\Trabalho_4\\texto.txt");

        var ref = new Object() {
            int totalLetras = 0;
            int totalNumeros = 0;
        };

        try (Scanner scanner = new Scanner(path)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                for (char c : linha.toCharArray()) {
                    if (Character.isLetter(c)) {
                        ref.totalLetras++;
                    } else if (Character.isDigit(c)) {
                        ref.totalNumeros++;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo: " + e.getMessage());
            return;
        }

        // Exibe a telinha com o resultado
        SwingUtilities.invokeLater(() -> {
            Screen tela = new Screen(ref.totalLetras, ref.totalNumeros);
            tela.setVisible(true);
        });
    }
}
