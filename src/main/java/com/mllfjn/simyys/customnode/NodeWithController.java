package com.mllfjn.simyys.customnode;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;

public class NodeWithController extends BorderPane {
    private final TilePane controller = new TilePane();

    public NodeWithController() {
        super();
        setRight(controller);
        controller.setVgap(20);
        controller.setPadding(new Insets(20, 10, 20, 10));
    }

    public void setNode(Node node) {
        setCenter(node);
    }

    public void addControlButton(String text, EventHandler<ActionEvent> value) {
        Button button = new Button(text);
        button.setOnAction(value);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        controller.getChildren().add(button);
    }
}
