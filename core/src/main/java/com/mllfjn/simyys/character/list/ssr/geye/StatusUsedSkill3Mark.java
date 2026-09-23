package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;

class StatusUsedSkill3Mark extends Status {
    public StatusUsedSkill3Mark(Character character) {
        super("特殊-葛叶标记", character);
        duration(StatusDurationType.CHI_XU, 2);
    }
}
