package com.ufrn.middleware.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientStub {
    private final String nameServiceHost;
    private final int nameServicePort;

    public ClientStub(String nameServiceHost, int nameServicePort) {
        this.nameServiceHost = nameServiceHost;
        this.nameServicePort = nameServicePort;
    }

    public void sendMessage(String serviceName, String message) {
        communicateWithService(serviceName, message);
    }

    public void getMessages(String serviceName) {
        communicateWithService(serviceName, "GET MESSAGES");
    }

    private void communicateWithService(String serviceName, String message) {
        try {
            // Conexão com o serviço de nomes para obter detalhes do serviço
            Socket nameSocket = new Socket(nameServiceHost, nameServicePort);
            PrintWriter nameOut = new PrintWriter(nameSocket.getOutputStream(), true);
            BufferedReader nameIn = new BufferedReader(new InputStreamReader(nameSocket.getInputStream()));

            nameOut.println(serviceName);
            String serviceDetails = nameIn.readLine();  // espera receber "host:port"
            nameSocket.close();

            if (serviceDetails == null || serviceDetails.equals("Service not found")) {
                System.out.println("Service not found or no response from name service.");
                return;
            }

            String[] details = serviceDetails.split(":");
            String serviceHost = details[0];
            int servicePort = Integer.parseInt(details[1]);

            // Conexão com o serviço real
            Socket serviceSocket = new Socket(serviceHost, servicePort);
            PrintWriter serviceOut = new PrintWriter(serviceSocket.getOutputStream(), true);
            BufferedReader serviceIn = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));

            // Envio da mensagem ou solicitação para o serviço
            serviceOut.println(message);

            // Leitura das respostas do serviço
            String response;
            while (true) {
                response = serviceIn.readLine();
                if (response == null) { // Se `null`, o servidor fechou a conexão
                    break;
                }
                System.out.println("Server response: " + response);
            }

            serviceSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
