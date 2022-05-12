module com.example.lulang_leakha {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.lulang_leakha to javafx.fxml;
    exports com.example.lulang_leakha;
}