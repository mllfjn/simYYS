package com.mllfjn.simyys.starter;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainStarter extends Application {
    @Override
    public void start(Stage stage) {
        new Initializer(stage);
    }
    public static void main(String[] args) {
        launch();
    }
}
