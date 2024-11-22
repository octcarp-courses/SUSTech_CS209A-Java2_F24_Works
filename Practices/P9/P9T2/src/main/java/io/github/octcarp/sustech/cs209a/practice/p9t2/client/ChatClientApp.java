package io.github.octcarp.sustech.cs209a.practice.p9t2.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ChatClientApp extends Application {
    private static ServerHandler serverHandler;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatClientApp.class.getResource("/client-view.fxml"));
        Parent root = fxmlLoader.load();


        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter your name:");

        Optional<String> result = dialog.showAndWait();
        String userName = result.orElse("null");


        ChatClientController controller = fxmlLoader.getController();
        controller.setUserName(userName);

        serverHandler = new ServerHandler(controller, userName);

        Scene scene = new Scene(root, 320, 240);
        stage.setTitle(userName);
        stage.setScene(scene);

        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    MyExit();
                }
            });
        });
    }

    public static ServerHandler getServerHandler() {
        return ChatClientApp.serverHandler;
    }

    private void MyExit() {
        serverHandler.closeSocket();
        Platform.exit();
    }
}
