package master;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

public class Master {
    private static final String SLAVE1 = "http://slave_letters:8001";
    private static final String SLAVE2 = "http://slave_numbers:8002";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(5000), 0);
        server.createContext("/processar", new ProcessarHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        System.out.println("Mestre ouvindo na porta 5000...");
    }

    static class ProcessarHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("[MASTER] Requisição recebida de " + exchange.getRemoteAddress());
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStream input = exchange.getRequestBody();
            String texto = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("[MASTER] Texto recebido do cliente: \"" + texto + "\"");

            ExecutorService executor = Executors.newFixedThreadPool(2);

            Future<Integer> letrasFuture = executor.submit(() -> processarEscravo(SLAVE1, texto, "/letras"));
            Future<Integer> numerosFuture = executor.submit(() -> processarEscravo(SLAVE2, texto, "/numeros"));

            int totalLetras, totalNumeros;

            try {
                totalLetras = letrasFuture.get(10, TimeUnit.SECONDS);
                System.out.println("[MASTER] Resposta do escravo de letras: " + totalLetras);
                totalNumeros = numerosFuture.get(10, TimeUnit.SECONDS);
                System.out.println("[MASTER] Resposta do escravo de números: " + totalNumeros);
            } catch (Exception e) {
                String erro = "Erro ao comunicar com os escravos: " + e.getMessage();
                exchange.sendResponseHeaders(500, erro.length());
                exchange.getResponseBody().write(erro.getBytes());
                exchange.getResponseBody().close();
                return;
            } finally {
                executor.shutdown();
            }

            String resposta = totalLetras + "\n" + totalNumeros;
            exchange.sendResponseHeaders(200, resposta.length());
            exchange.getResponseBody().write(resposta.getBytes());
            exchange.getResponseBody().close();

            System.out.println("[MASTER] Resposta enviada ao cliente: \n" + resposta);
        }

        private int processarEscravo(String urlBase, String texto, String rota) {
            try {
                // Verifica disponibilidade
                HttpURLConnection checkConn = (HttpURLConnection) new URL(urlBase + "/disponivel").openConnection();
                checkConn.setRequestMethod("GET");

                int status = checkConn.getResponseCode();
                String disponibilidade = new String(checkConn.getInputStream().readAllBytes()).trim();

                if (status != 200 || !"sim".equalsIgnoreCase(disponibilidade)) {
                    System.out.println("Escravo em " + urlBase + " está indisponível.");
                    return -1;
                }

                // Envia texto
                HttpURLConnection conn = (HttpURLConnection) new URL(urlBase + rota).openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "text/plain");

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(texto.getBytes());
                }

                if (conn.getResponseCode() == 200) {
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()))) {
                        return Integer.parseInt(reader.readLine().trim());
                    }
                }else{
                    System.out.println("[MASTER] Escravo " + urlBase + " respondeu com código HTTP " + conn.getResponseCode());
                }
            } catch (Exception e) {
                System.out.println("[MASTER] Erro com escravo " + urlBase + ": " + e.getMessage());
            }
            return -1;
        }
    }
}
