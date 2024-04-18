package com.ufrn.middleware.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoService {
    private int port;

    public EchoService(int port) {
        this.port = port;
    }

    public void startService() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Service started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Received from client: " + inputLine);
                        out.println("Echo from service: " + inputLine);
                    }
                    System.out.println("Closing connection.");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    clientSocket.close();  // Assegura que o socket do cliente seja fechado
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
