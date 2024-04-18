package com.ufrn.middleware.serviceRegistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NameServiceApplication {
    private static final int PORT = 9090;
    private static Map<String, String> serviceRegistry = new HashMap<>();

    public static void main(String[] args) {
        // Configura os serviços disponíveis
        serviceRegistry.put("EchoService", "localhost:8080");  // Serviço existente para exemplo
        serviceRegistry.put("MessageService", "localhost:8081");  // Novo serviço de mensagens

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Name Service running on port " + PORT);

            while (true) {
                final Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                        String serviceName = in.readLine();
                        String serviceDetails = serviceRegistry.getOrDefault(serviceName, "Service not found");
                        out.println(serviceDetails);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
