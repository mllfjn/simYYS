package com.mllfjn.simyys.customnode;

import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CustomTextFlow extends ScrollPane {
    final TextFlow textFlow = new TextFlow();
    public CustomTextFlow() {
        super();
        this.setContent(textFlow);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.widthProperty().addListener((obs, old, val) -> textFlow.setMaxWidth(val.doubleValue()));
        textFlow.heightProperty().addListener((obs, old, val) -> setVvalue(1));
    }

    public void addText(String s) {
        this.addText("\t" + s + "\n", NumberType.NORMAL, FontSize.NORMAL);
    }

    public void addTextTop(String s) {
        this.addText(s + "\n", NumberType.NORMAL, FontSize.BIG);
    }

    public void addText(String s, NumberType color, FontSize fontSize) {
        Text text = new Text(s);
        text.setFont(new Font(fontSize.size));
        text.setFill(color.color);
        textFlow.getChildren().add(text);
    }

    public enum NumberType {
        NORMAL(Color.BLACK),
        ATTACK(Color.RED),
        CRITICAL(Color.ORANGE),
        HEAL(Color.GREEN);
        final Color color;
        NumberType(Color color) {
            this.color = color;
        }
    }

    public enum FontSize {
        BIG(20),
        NORMAL(15);

        final double size;
        FontSize(double size) {
            this.size = size;
        }
    }
}
