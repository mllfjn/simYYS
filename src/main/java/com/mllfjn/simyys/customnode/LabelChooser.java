package com.mllfjn.simyys.customnode;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LabelChooser extends Label {

    private static final int width = 75;
    private static final int nameWidth = 100;
    private static final int height = 25;
    public LabelChooser(String text, String[] types, String[][] context) {
        super(text);

        this.setAlignment(Pos.CENTER);
        this.setPrefSize(width, height);
        this.setOnMouseClicked(mouseEvent -> {
            Stage stage = new Stage();
            VBox root = new VBox(10);

            for (int i = 0; i < types.length; i++) {
                Label typeLabel = new Label(types[i]);
                typeLabel.setAlignment(Pos.CENTER);
                typeLabel.setPrefSize(nameWidth, height);
                FlowPane flowPane = new FlowPane();
                for (String s : context[i]) {
                    Button button = new Button(s);
                    button.setAlignment(Pos.CENTER);
                    button.setPrefSize(nameWidth, height);
                    button.setOnAction(event -> {
                        this.setText(s);
                        stage.close();
                    });
                    flowPane.getChildren().add(button);
                }
                BorderPane borderPane = new BorderPane();
                borderPane.setLeft(typeLabel);
                borderPane.setCenter(flowPane);
                root.getChildren().add(borderPane);
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(text);
            stage.show();
        });
    }
}
