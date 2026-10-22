package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusShenLi extends Status {
    // 上限5层,每层提升自身20%效果抵抗,并根据已有层数强化与世结缘(3)效果
    private StatusShenLi(Character character) {
        super("神力", character);
        type(StatusType.BUFF, StatusForm.YIN_JI);
        enableStack(5);
        attribute(Attribute.EFFECT_RESIST_RATE, _ -> 20.0 * getStack());
        displayNameAndStack();
        addTo();
    }

    public static void addStack(Character character, int amount) {
        StatusShenLi shenLi = character.getStatus(StatusShenLi.class).orElseGet(() -> new StatusShenLi(character));
        shenLi.addStack(amount);
    }
}
