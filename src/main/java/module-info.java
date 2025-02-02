module com.mllfjn.simyys {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.mllfjn.simyys to javafx.fxml;
    opens com.mllfjn.simyys.starter to com.google.gson;
    exports com.mllfjn.simyys;
    exports com.mllfjn.simyys.starter;
    exports com.mllfjn.simyys.starter.info;
    exports com.mllfjn.simyys.character;
}