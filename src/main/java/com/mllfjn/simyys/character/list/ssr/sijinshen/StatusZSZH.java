package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusZSZH extends StatusQMCauseAttackListener {
    private static final String StatusName = "智识之火";

    public StatusZSZH(Character from, Character belongTo, int critPower, SkillQiMeng qm) {
        super(StatusName, qm, from, belongTo);
        type(StatusType.BUFF, StatusForm.YIN_JI);
        qm.addCharacter(belongTo);
        beforeDelete(() -> qm.removeCharacter(belongTo));
        attribute(Attribute.CRIT_POWER, critPower);
        displayName();
    }
}
