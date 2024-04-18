package com.ufrn.middleware.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private int port;
    private List<String> messages = new ArrayList<>();

    public MessageService(int port) {
        this.port = port;
    }

    public void startService() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Message Service started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        if ("GET MESSAGES".equals(inputLine.toUpperCase())) {
                            // Cliente solicita todas as mensagens
                            messages.forEach(out::println);
                            out.println("END OF MESSAGES");
                        } else {
                            // Cliente envia uma nova mensagem
                            messages.add(inputLine);
                            out.println("Message received: " + inputLine);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    clientSocket.close(); // Garante que o socket seja fechado após o uso
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
