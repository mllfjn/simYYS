package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill5 extends Skill {
    private static final String SkillName = "下潜";

    private final Skill8 skill8;

    public Skill5(Character belongTo, Skill8 skill8) {
        super(belongTo, -1, 0, 0, 5);
        this.skill8 = skill8;
    }

    void use() {

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
