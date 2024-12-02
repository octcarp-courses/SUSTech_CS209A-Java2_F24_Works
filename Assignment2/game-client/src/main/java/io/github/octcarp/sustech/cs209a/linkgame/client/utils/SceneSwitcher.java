package io.github.octcarp.sustech.cs209a.linkgame.client.utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneSwitcher {

    private static SceneSwitcher instance = new SceneSwitcher();

    private Stage primaryStage;

    private final Map<String, Scene> scenes = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();

    private SceneSwitcher() {
    }

    public static SceneSwitcher getInstance() {
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void switchScene(String sceneName) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + sceneName + ".fxml"));
                Parent root = loader.load();
                scenes.put(sceneName, new Scene(root));
                controllers.put(sceneName, loader.getController());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            primaryStage.setScene(scenes.get(sceneName));
        });
    }

    public Object getController(String sceneName) {
        return controllers.get(sceneName);
    }

    public void netErrAndReturn() {
        AlertPopper.popError("Network", "Network Error", "Please check your network, or server is down");
        switchScene("login");
    }
}
