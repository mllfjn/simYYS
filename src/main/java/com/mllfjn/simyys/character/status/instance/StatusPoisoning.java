package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

public class StatusPoisoning extends Status {
    private final int level;

    public StatusPoisoning(Character from, Character belongTo, int level, int duration) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        this.level = level;

        setDurationType(StatusDurationType.CHI_XU, duration);
    }

    public double getDefenseForJianJieShangHai() {
        return -10 * level;
    }
}
