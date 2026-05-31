package com.mllfjn.simyys.character.list.sp.sphongye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "枫起之舞";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
        if (level >= 5) {
            belongTo.bp.addPriorityMove(belongTo, this::useWithoutCost);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        StatusLinYin.install(((SPHongYe) getBelongTo()), getLevel());
        return Optional.empty();
    }
}
