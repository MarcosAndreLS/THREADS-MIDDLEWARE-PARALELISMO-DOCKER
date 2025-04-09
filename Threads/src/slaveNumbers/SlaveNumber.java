package slaveNumbers;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SlaveNumber {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8002), 0);
        server.createContext("/disponivel", new DisponivelHandler());
        server.createContext("/numeros", new NumerosHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Escravo 2 rodando na porta 8002...");
    }

    static class DisponivelHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = "sim";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

    static class NumerosHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStream input = exchange.getRequestBody();
            String texto = new String(input.readAllBytes(), StandardCharsets.UTF_8);

            long count = texto.chars().filter(Character::isDigit).count();

            String response = String.valueOf(count);
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }
}
