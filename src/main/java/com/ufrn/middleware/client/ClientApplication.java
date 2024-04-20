package com.ufrn.middleware.client;

import java.util.Scanner;

public class ClientApplication {
    public static void main(String[] args) {
        // Endereço e porta do serviço de nomes
        String nameServiceHost = "localhost";
        int nameServicePort = 9090;

        // Instancia o ClientStub com o endereço e porta do serviço de nomes
        ClientStub stub = new ClientStub(nameServiceHost, nameServicePort);

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nClient is running. Type 'exit' to quit.");
        System.out.println("Use format: <service name> <message> to send a message to the Bot.");

        while (true) {
            System.out.print("Enter message: ");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input.trim())) {
                break;  // Exit the loop and end the program
            }
            else {
                String[] parts = input.split(" ", 2);
                if (parts.length < 2) {
                    System.out.println("Invalid command. Please use the format: <service name> <message>");
                    continue;
                }
                String serviceName = parts[0];
                String message = parts[1];
                stub.sendMessage(serviceName, message);
            }
        }

        scanner.close();
        System.out.println("Client stopped.");
    }
}
