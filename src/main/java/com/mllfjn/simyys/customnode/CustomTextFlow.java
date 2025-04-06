package com.mllfjn.simyys.customnode;

import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CustomTextFlow extends ScrollPane {
    TextFlow textFlow = new TextFlow();
    public CustomTextFlow() {
        super();
        this.setContent(textFlow);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.widthProperty().addListener((obs, old, val) -> {
            textFlow.setMaxWidth(val.doubleValue());
        });
    }

    public void addText(String s) {
        this.addText(s, TextColor.NORMAL, 20);
    }

    public void addText(String s, TextColor color, double fontSize) {
        Text text = new Text(s);
        text.setFont(new Font(fontSize));
        text.setFill(color.color);
        textFlow.getChildren().add(text);
    }

    public enum TextColor {
        NORMAL(Color.BLACK),
        ATTACK(Color.RED),
        CRITICAL(Color.ORANGE),
        HEAL(Color.GREEN);
        final Color color;
        TextColor(Color color) {
            this.color = color;
        }
    }
}
