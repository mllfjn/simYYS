package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill6 extends Skill {
    private static final String SkillName = "飞天一击";

    public Skill6(Character belongTo) {
        super(belongTo, -1, 0, 0, 6);
    }

    @Override
    public String getSkillDesc() {
        return "没做,不至于真让它飞走吧";
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }
}
