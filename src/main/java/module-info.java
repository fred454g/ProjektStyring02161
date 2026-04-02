module hellofx {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
 
    opens dtu.example.ui to javafx.fxml; // Gives access to fxml files
    exports dtu.example.ui; // Exports the class inheriting from javafx.application.Application
    exports dtu.example.domain;
}