package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;

public class StatusBoss extends Status implements IgnoreDebuff, IgnoreActionDecrease {
    public StatusBoss(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }
}
