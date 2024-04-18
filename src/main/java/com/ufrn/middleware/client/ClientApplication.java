package com.ufrn.middleware.client;

public class ClientApplication {
    public static void main(String[] args) {
        // Endereço e porta do serviço de nomes
        String nameServiceHost = "localhost";
        int nameServicePort = 9090;

        // Instancia o ClientStub com o endereço e porta do serviço de nomes
        ClientStub stub = new ClientStub(nameServiceHost, nameServicePort);;
        String echoServiceName = "EchoService";
        String messageServiceName = "MessageService";

        // Envia algumas mensagens para o echo service
        stub.sendMessage(echoServiceName, "Hello, Echo Service!");

        // Envia algumas mensagens
//        stub.sendMessage(messageServiceName, "Hello, Message Service!");
//        stub.sendMessage(messageServiceName, "Another message!");

        // Recupera todas as mensagens
//        System.out.println("Retrieving messages:");
//        stub.getMessages(messageServiceName);
    }
}
