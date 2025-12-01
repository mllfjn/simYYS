package com.mllfjn.simyys.customnode;

import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CustomText extends Text {
    private final TextFlowLog.TextType type;
    public CustomText(String text, TextFlowLog.TextType type, TextFlowLog.TextColor textColor, TextFlowLog.FontSize fontSize) {
        super(text);
        this.type = type;
        this.setFont(new Font(fontSize.size));
        this.setFill(textColor.color);
    }

    public CustomText(String text, String tooltipText, TextFlowLog.TextType type, TextFlowLog.TextColor textColor, TextFlowLog.FontSize fontSize) {
        this(text, type, textColor, fontSize);
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setShowDuration(Duration.INDEFINITE);
        Tooltip.install(this, tooltip);
    }

    public TextFlowLog.TextType getType() {
        return type;
    }
}
