package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

public class StatusDeepFrozen extends Status implements CrowdControl {
    private StatusDeepFrozen(Character from, Character belongTo) {
        super("深度冰冻", from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        duration(StatusDurationType.CHI_XU, 1);
        displayName();
        attribute(Attribute.SPEED, -20);
    }

    public static void install(Character from, Character belongTo) {
        if (!belongTo.isHaveStatus(StatusDeepFrozen.class)) {
            belongTo.addStatus(new StatusDeepFrozen(from, belongTo));
        }
    }
}
