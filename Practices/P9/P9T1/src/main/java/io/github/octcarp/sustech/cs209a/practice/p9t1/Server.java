package io.github.octcarp.sustech.cs209a.practice.p9t1;

import java.io.*;
import java.net.*;
import org.json.*;
import java.net.http.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server waiting for clients to connect...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String pokemonName;
            while ((pokemonName = in.readLine()) != null) {
                if (pokemonName.equalsIgnoreCase("QUIT")) {
                    out.println("Client quits.");
                    break;
                }

                String info = fetchPokemonInfo(pokemonName);
                out.println(info);
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

    private String fetchPokemonInfo(String name) {
        String apiUrl = "https://pokeapi.co/api/v2/pokemon/" + name.toLowerCase();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());

                int height = json.getInt("height");
                int weight = json.getInt("weight");

                List<String> abilities = new ArrayList<>();
                JSONArray abilitiesArray = json.getJSONArray("abilities");
                for (int i = 0; i < abilitiesArray.length(); i++) {
                    String ability = abilitiesArray.getJSONObject(i).getJSONObject("ability").getString("name");
                    abilities.add(ability);
                }

                return String.format("Name: %s\nHeight: %d\nWeight: %d\nAbilities: %s",
                        name, height, weight, abilities);
            } else {
                return "Pokémon not found!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching data from API.";
        }
    }
}