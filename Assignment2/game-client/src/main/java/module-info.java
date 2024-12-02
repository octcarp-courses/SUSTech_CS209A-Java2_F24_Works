module linkgame.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive linkgame.common;

    opens io.github.octcarp.sustech.cs209a.linkgame.client to javafx.fxml;
    exports io.github.octcarp.sustech.cs209a.linkgame.client;

    opens io.github.octcarp.sustech.cs209a.linkgame.client.controller to javafx.fxml;
    exports io.github.octcarp.sustech.cs209a.linkgame.client.controller;

    opens io.github.octcarp.sustech.cs209a.linkgame.client.net to javafx.fxml;
    exports io.github.octcarp.sustech.cs209a.linkgame.client.net;
}