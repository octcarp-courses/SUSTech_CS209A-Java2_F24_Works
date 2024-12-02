package io.github.octcarp.sustech.cs209a.linkgame.client;

import io.github.octcarp.sustech.cs209a.linkgame.client.net.ClientService;
import io.github.octcarp.sustech.cs209a.linkgame.client.net.LoginData;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.AlertPopper;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class GameMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
    }

    @Override
    public void start(Stage primaryStage) {
        // init primary stage settings
        initPrimaryStage(primaryStage);

        // show login scene finally
        SceneSwitcher.getInstance().setPrimaryStage(primaryStage);
        SceneSwitcher.getInstance().switchScene("login");  // Default login scene
        primaryStage.show();
    }

    private void initPrimaryStage(Stage primaryStage) {
        // set window title & icon
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(GameMain.class.getResource(
                "/img/main_icon.png")).toExternalForm()));
        primaryStage.setTitle("CS209A Linking Game");

        // handle the close event
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("Your match step will be lost.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    MyExit();
                }
            });
        });
    }

    private void MyExit() {
        AlertPopper.setTryExit(true);

        Request request = new Request(RequestType.SHUTDOWN);
        ClientService.getInstance().sendRequest(request);

        Platform.exit();
        System.exit(0);
    }
}