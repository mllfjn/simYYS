package com.mllfjn.simyys.guihuo;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.io.Serializable;

public class GuiHuo implements Serializable {
    private int max = 8;
    private int now;
    private int increment = 3;
    private int progress;
    private transient Label guiHuoDisplay;

    public GuiHuo(int startWith) {
        now = startWith;
    }

    public Node getGuiHuoDisplay() {
        guiHuoDisplay = new Label();
        guiHuoDisplay.setFont(new Font(30));
        repaint();
        StackPane stackPane = new StackPane(guiHuoDisplay);
        stackPane.setAlignment(Pos.BOTTOM_CENTER);
        return stackPane;
    }

    public boolean canUseGuiHuo(int num) {
        return now >= num;
    }

    public void useGuiHuo(int num) {
        now -= num;
        repaint();
    }

    public void addProgress() {
        progress++;
        if (progress == 5) {
            progress = 0;
            gainGuiHuo(increment);
            if (increment < 5) {
                increment++;
            }
        }
        repaint();
    }

    public void gainGuiHuo(int num) {
        now = Math.min(now + num, max);
        repaint();
    }

    public int getNow() {
        return now;
    }

    public void setMax(int num) {
        max = num;
        now = Math.min(now, max);
        repaint();
    }

    private void repaint() {
        // 当前鬼火：4/8  进度：2/5(+4)
        if (guiHuoDisplay != null) {
            guiHuoDisplay.setText("当前鬼火:" + now + "/" + max + "-进度:" + progress + "/5(+" + increment + ")");
        }
    }
}
