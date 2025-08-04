package com.mllfjn.simyys.customnode;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.Serializable;

public class CustomText extends Text implements Serializable {
    private final TextFlowLog.TextType type;
    public CustomText(String s, TextFlowLog.TextType type, TextFlowLog.TextColor textColor, TextFlowLog.FontSize fontSize) {
        super(s);
        this.type = type;
        this.setFont(new Font(fontSize.size));
        this.setFill(textColor.color);
    }

    public TextFlowLog.TextType getType() {
        return type;
    }
}
