package com.mllfjn.simyys.customnode;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class LabelChooser extends Label {
    private static final int width = 75;
    private static final int height = 25;
    public LabelChooser(String text) {
        super(text);

        this.setAlignment(Pos.CENTER);
        this.setPrefSize(width, height);
        this.setOnMouseClicked(mouseEvent -> onMouseClicked());
    }

    /*public void onMouseClicked() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        StringGroup[] groups = getStringGroups();

        for (int i = 0; i < groups.length; i++) {
            Label typeLabel = new Label(groups[i].label());
            typeLabel.setAlignment(Pos.CENTER);
            typeLabel.setPrefSize(100, 25);
            FlowPane flowPane = new FlowPane();
            for (String s : list[i]) {
                Button button = new Button(s);
                button.setAlignment(Pos.CENTER);
                button.setPrefSize(100, 25);
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
        stage.setTitle(getChooseText());
        stage.show();
    }*/

    public void onMouseClicked() {
        StringGroup[] groups = getStringGroups();

        Stage stage = new Stage();
        GridPane gp = new GridPane();

        for (StringGroup group : groups) {
            Label typeLabel = new Label(group.label());
            typeLabel.setAlignment(Pos.CENTER);
            typeLabel.setPrefSize(100, 25);
            FlowPane flowPane = new FlowPane();
            for (String s : group.values()) {
                Button button = new Button(s);
                button.setAlignment(Pos.CENTER);
                button.setOnAction(event -> {
                    this.setText(s);
                    stage.close();
                });
                flowPane.getChildren().add(button);
            }
        }
        stage.setScene(new Scene(gp));
        stage.setTitle(getChooseText());
        stage.showAndWait();
    }
    protected abstract StringGroup[] getStringGroups();
    protected abstract String getChooseText();
}
