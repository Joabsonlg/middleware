package com.ufrn.middleware.server;


import org.alicebot.ab.Chat;

import java.io.File;

public class ServerApplication {
    public static void main(String[] args) {
        // Portas para cada serviço
        int chatBotServicePort = 8080;
        int messageServicePort = 8081;

        Thread chatBotServiceThread = new Thread(() -> {
            ChatBotService chatBotService = new ChatBotService(chatBotServicePort);
            ChatBotService.registerService("ChatBotService", "localhost", chatBotServicePort);
            chatBotService.startService();
        });

        chatBotServiceThread.start();
    }
}
