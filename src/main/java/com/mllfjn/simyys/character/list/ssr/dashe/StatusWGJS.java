package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

public class StatusWGJS extends Status {
    public StatusWGJS(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        belongTo.sealPassiveSkill();
        belongTo.sealYuHun();
    }

    @Override
    public void beforeDelete() {
        belongTo.unsealPassiveSkill();
        belongTo.unsealYuHun();
    }
}
