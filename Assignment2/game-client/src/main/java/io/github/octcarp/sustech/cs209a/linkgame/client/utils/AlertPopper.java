package io.github.octcarp.sustech.cs209a.linkgame.client.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertPopper {
    private static boolean tryExit = false;

    public static void setTryExit(boolean tryExit) {
        AlertPopper.tryExit = tryExit;
    }

    public static void popError(String type, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(type + " Error");
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void popInfo(String type, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(type + " Information");
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void popWarning(String type, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(type + " Warning");
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    public static void popNetErrAndExit() {
        if (tryExit) {
            return;
        }
        Platform.runLater(() -> {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Network Error");
            alert.setHeaderText("Network Error");
            alert.setContentText("Please check your network, or server is down");
            alert.showAndWait();
            SceneSwitcher.getInstance().switchScene("login");
        });
    }
}
