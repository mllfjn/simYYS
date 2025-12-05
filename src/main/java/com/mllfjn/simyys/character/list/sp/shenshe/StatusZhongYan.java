package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;

public class StatusZhongYan extends Status implements IgnoreDebuff {
    public StatusZhongYan(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }
}
