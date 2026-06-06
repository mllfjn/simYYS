package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusMo extends Status implements Displayable {
    private static final String StatusName = "魔";

    private int stack = 1;

    StatusMo(Character from, Character belongTo) {
        super(from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
    }

    void addStack(Skill2 skill2) {
        if (stack == 2) {
            from.doInteractive(interactive -> {
                AttackInfo attackInfo = AttackInfo.createJianJieAttack(from, skill2, belongTo, from.getAttack());
                attackInfo.setMultiplier(211);
                interactive.attack(attackInfo);

            });
            delete();
        } else {
            stack++;
        }
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }
}
