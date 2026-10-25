package com.mllfjn.simyys.character.list.ssr.yunwaijing;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.instance.StatusShield;

class StatusJingDun extends StatusShield {
    public StatusJingDun(com.mllfjn.simyys.character.Character from, Character belongTo) {
        super("镜盾", from, belongTo, from.getDefence() * 6.8);
        duration(StatusDurationType.CHI_XU, 2);
    }
}
