module p {
    requires javafx.controls;
    requires javafx.fxml;


    opens io.github.octcarp.sustech.cs209a.proj.p10 to javafx.fxml;
    exports io.github.octcarp.sustech.cs209a.proj.p10;
}