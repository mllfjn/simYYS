package com.mllfjn.simyys.character.list.ssr.shenwuyue;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import javafx.scene.paint.Color;

public class StatusMeiMengBiCheng extends Status implements Displayable {
    private static final String StatusName = "美梦必成";

    public StatusMeiMengBiCheng(Character from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        setDurationType(StatusDurationType.CHI_XU, 1);
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }

    @Override
    public Color getColor(StatusType type) {
        return GOOD_COLOR;
    }
}
