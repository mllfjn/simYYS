package com.mllfjn.simyys.character.list.sp.sphongye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "枫起之舞";

    private final Skill2 skill2;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 3, 0, 3);
        this.skill2 = skill2;

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
        StatusLinYin.install(((SPHongYe) getBelongTo()), getLevel(), skill2);
        return Optional.empty();
    }
}
