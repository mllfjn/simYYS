package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;

public class StatusConfusion extends Status implements CrowdControl, Displayable {
    public static final String StatusName = "混乱";

    public StatusConfusion(Character from, Character belongTo, int duration) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        setDurationType(StatusDurationType.CHI_XU, duration);
    }

    public void doConfusion() {
        // 被混乱的敌方在行动时,强制使用普攻攻击场上随机目标
        Character target = new CharacterFinder(belongTo)
                .filterSelf()
                .getRandom();

        belongTo.getPuGong().ifPresent(
                skill1 -> skill1.usePrivate(belongTo.getInteractive(), target));
    }

    @Override
    public String getDisplayText() {
        return StatusName + getDuration();
    }
}
