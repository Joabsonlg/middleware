package com.ufrn.middleware.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClientStub {
    private final String nameServiceHost;
    private final int nameServicePort;

    public ClientStub(String nameServiceHost, int nameServicePort) {
        this.nameServiceHost = nameServiceHost;
        this.nameServicePort = nameServicePort;
    }

    public void sendMessage(String serviceName, String message) {
        communicateWithService(serviceName, Arrays.asList(message));
    }

    public void communicateWithService(String serviceName, List<String> messages) {
        try {
            // Conexão com o serviço de nomes para obter detalhes do serviço
            Socket nameServiceSocket = new Socket(this.nameServiceHost, this.nameServicePort);
            PrintWriter nameServiceOut = new PrintWriter(nameServiceSocket.getOutputStream(), true);
            BufferedReader nameServiceIn = new BufferedReader(new InputStreamReader(nameServiceSocket.getInputStream()));

            nameServiceOut.println(serviceName);
            String serviceDetails = nameServiceIn.readLine(); // Recebe "host:porta" do serviço
            nameServiceSocket.close();

            if (serviceDetails == null || serviceDetails.equals("Service not found")) {
                System.out.println("Service not found or no response from name service.");
                return;
            }

            String[] address = serviceDetails.split(":");
            Socket serviceSocket = new Socket(address[0], Integer.parseInt(address[1]));
            PrintWriter serviceOut = new PrintWriter(serviceSocket.getOutputStream(), true);
            BufferedReader serviceIn = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));

            for (String message : messages) {
                serviceOut.println(message);
                String response = serviceIn.readLine();  // Lê a resposta para cada mensagem enviada
                System.out.println("Server response: " + response);
            }

            serviceSocket.close();  // Fecha a conexão após completar todas as transações
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
