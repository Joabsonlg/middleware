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
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Name Service running on port " + PORT);

            while (true) {
                final Socket clientSocket = serverSocket.accept();
                new Thread(() -> {
                    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                        String inputLine = in.readLine();
                        if (inputLine.contains(":")) {
                            // Assume it's a registration message
                            String[] parts = inputLine.split(" ", 2);
                            serviceRegistry.put(parts[0], parts[1]);  // parts[0] = ServiceName, parts[1] = host:port
                            out.println("Service registered successfully " + parts[0]);
                        } else {
                            // It's a lookup
                            String serviceDetails = serviceRegistry.getOrDefault(inputLine, "Service not found");
                            out.println(serviceDetails);
                        }
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
