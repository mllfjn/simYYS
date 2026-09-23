package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

public class StatusWGJS extends Status {
    public StatusWGJS(Character from, Character belongTo) {
        super("五感尽失", from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        belongTo.sealPassiveSkill();
        belongTo.sealYuHun();

        beforeDelete(() -> {
            belongTo.unsealPassiveSkill();
            belongTo.unsealYuHun();
        });
    }
}
