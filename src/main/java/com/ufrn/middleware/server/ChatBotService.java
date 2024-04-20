package com.ufrn.middleware.server;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicBooleans;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatBotService {
    private int port;
    private Bot bot;
    private Chat chatSession;
    private static final String NAME_SERVICE_HOST = "localhost";
    private static final int NAME_SERVICE_PORT = 9090;

    public ChatBotService(int port) {
        this.port = port;
        this.bot = new Bot("super", getResourcesPath());
        this.chatSession = new Chat(bot);
        MagicBooleans.trace_mode = false;
    }

    public void startService() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("ChatBot Service started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String response = chatSession.multisentenceRespond(inputLine);
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void registerService(String serviceName, String serviceHost, int servicePort) {
        try (Socket socket = new Socket(NAME_SERVICE_HOST, NAME_SERVICE_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String registrationMessage = serviceName + " " + serviceHost + ":" + servicePort;
            out.println(registrationMessage);
            String response = in.readLine(); // Espera confirmação do serviço de nomes
            System.out.println("Registration response: " + response);
        } catch (IOException e) {
            System.out.println("Error registering service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 1);
        String resourcesPath = path + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath;
    }

}
