package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.status.Status;
import com.mllfjn.simyys.status.StatusForm;
import com.mllfjn.simyys.status.StatusType;
import com.mllfjn.simyys.status.determinant.IgnoreDebuff;

public class StatusZhongYan extends Status implements IgnoreDebuff {
    public StatusZhongYan(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }
}
