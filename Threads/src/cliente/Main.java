package cliente;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("D:\\Faculdade\\Sistemas_Distribuidos\\Trabalho_4\\texto.txt");
        String conteudo;

        try {
            conteudo = Files.readString(path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo: " + e.getMessage());
            return;
        }

        // Enviar para o servidor mestre
        int totalLetras = 0;
        int totalNumeros = 0;
        try {
            URL url = new URL("http://host.docker.internal:5000/processar"); // coloque o IP ou hostname do container mestre aqui
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(conteudo.getBytes());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    if (scanner.hasNextLine()) totalLetras = Integer.parseInt(scanner.nextLine());
                    if (scanner.hasNextLine()) totalNumeros = Integer.parseInt(scanner.nextLine());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Erro na resposta do servidor: " + responseCode);
                return;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao enviar requisição: " + e.getMessage());
            return;
        }

        int finalTotalLetras = totalLetras;
        int finalTotalNumeros = totalNumeros;

        SwingUtilities.invokeLater(() -> {
            Screen tela = new Screen(finalTotalLetras, finalTotalNumeros);
            tela.setVisible(true);
        });
    }
}
