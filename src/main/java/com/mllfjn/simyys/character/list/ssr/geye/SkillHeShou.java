package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class SkillHeShou extends Skill {
    static final String SkillName = "合守";

    private final GeYe geYe;
    private final boolean isIncreaseSpeed;

    SkillHeShou(GeYe geYe, Character belongTo, boolean isIncreaseSpeed) {
        super(belongTo, -1, 0, 0, -1);

        this.geYe = geYe;
        this.isIncreaseSpeed = isIncreaseSpeed;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        StatusHZBH.addStack(geYe, getBelongTo(), 3, isIncreaseSpeed);
        return Optional.empty();
    }
}
