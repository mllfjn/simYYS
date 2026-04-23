package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusHZBH extends Status implements InfluenceDamageBeingAttack, Displayable {
    private static final String StatusName = "狐族庇护";

    private int stack;

    StatusHZBH(Character from, Character belongTo) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }

    @Override
    public void doInfluenceBeingAttack(AttackInfo attackInfo) {

    }
}
