module P_nine {
    requires javafx.controls;
    requires javafx.fxml;

    opens io.github.octcarp.sustech.cs209a.practice.p9t2.client to javafx.fxml;
    exports io.github.octcarp.sustech.cs209a.practice.p9t2.client;
}