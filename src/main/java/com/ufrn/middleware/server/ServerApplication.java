package com.ufrn.middleware.server;

public class ServerApplication {
    public static void main(String[] args) {
        // Portas para cada serviço
        int echoServicePort = 8080;
        int messageServicePort = 8081;

        // Cria uma thread para o EchoService
        Thread echoServiceThread = new Thread(() -> {
            EchoService echoService = new EchoService(echoServicePort);
            echoService.startService();
        });

        // Cria uma thread para o MessageService
        Thread messageServiceThread = new Thread(() -> {
            MessageService messageService = new MessageService(messageServicePort);
            messageService.startService();
        });

        // Inicia ambos os serviços
        echoServiceThread.start();
        messageServiceThread.start();
    }
}
