package com.mllfjn.simyys.utils;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;

public class PicUtil{
    public static Node clipAndStroke(ImageView imageView, double size, Paint color, double strokeWidth) {
        double radius = size / 2;

        imageView.setClip(new Circle(size / 2, size / 2, size / 2));
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        Circle border = new Circle(radius, radius, radius);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(color);
        border.setStrokeWidth(strokeWidth);

        StackPane container = new StackPane(border, imageView);
        container.setPadding(new Insets(strokeWidth));

        return container;
    }
    public static Image createPlaceholderImage(String text, double size) {
        Canvas canvas = new Canvas(size, size);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(0, 0, size, size);

        Text helper = new Text(text);
        helper.setFont(new Font(gc.getFont().getSize()));
        double width = helper.getLayoutBounds().getWidth();
        double scale = size / width * 0.8;
        gc.setFont(new Font(gc.getFont().getSize() * scale));
        gc.setFill(Color.BLACK);
        gc.fillText(text, (size - width * scale) / 2, size * 0.6);
        return canvas.snapshot(null, null);
        /*double fontSize = size / 6;


        Canvas canvas = new Canvas(size, size);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(0, 0, size, size);


        Text tt = new Text(text);
        Text tg = new Text("404");
        tt.setFont(new Font(fontSize));
        tg.setFont(new Font(fontSize));
        Bounds boundsText = tt.getLayoutBounds();
        Bounds boundsGeneral = tg.getLayoutBounds();

        gc.setFill(Color.BLACK);
        gc.setFont(new Font(fontSize));
        gc.fillText(text, (size - boundsText.getWidth()) / 2, size / 2);
        gc.setFont(new Font(fontSize));
        gc.fillText("404", (size - boundsGeneral.getWidth()) / 2, (size / 2 + boundsText.getHeight() + boundsGeneral.getHeight() / 2));

        return canvas.snapshot(null, null);*/
    }

    public static <T> Image loadImage(Class<T> clazz, String path, String name, String extension, double size) {
        URL url = clazz.getResource(path + name + extension);
        if (url == null) {
            return PicUtil.createPlaceholderImage(name, size);
        } else {
            return new Image(url.toString());
        }
    }
}
