package slaveLetters;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SlaveLetter {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/disponivel", new DisponivelHandler());
        server.createContext("/letras", new LetrasHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Escravo 1 rodando na porta 8001...");
    }

    static class DisponivelHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String response = "sim";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

    static class LetrasHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStream input = exchange.getRequestBody();
            String texto = new String(input.readAllBytes(), StandardCharsets.UTF_8);

            long count = texto.chars().filter(Character::isLetter).count();

            String response = String.valueOf(count);
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }
}
