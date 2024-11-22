package io.github.octcarp.sustech.cs209a.practice.p9t1;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            String pokemonName;
            while (true) {
                System.out.print("Enter the Pokémon name: ");
                pokemonName = scanner.nextLine();
                out.println(pokemonName);

                if (pokemonName.equalsIgnoreCase("QUIT")) {
                    break;
                }

                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    System.out.println(serverResponse);
                    if (serverResponse.contains("Abilities:")) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
