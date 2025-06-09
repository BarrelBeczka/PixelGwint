module org.example.pixelgwint {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.pixelgwint to javafx.fxml;
    exports org.example.pixelgwint;
}