package com.mllfjn.simyys.character.list.ssr.beimihu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

public class StatusShiZhiXi extends Status {
    public static final String StatusName = "时之隙";

    public StatusShiZhiXi(Character from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
    }
}
