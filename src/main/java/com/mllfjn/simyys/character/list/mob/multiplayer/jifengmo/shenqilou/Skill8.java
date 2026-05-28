package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill8 extends Skill {
    private static final String SkillName = "蜃气迷乱";

    public Skill8(Character belongTo) {
        super(belongTo, -1, 0, 0, 8);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return false;
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
