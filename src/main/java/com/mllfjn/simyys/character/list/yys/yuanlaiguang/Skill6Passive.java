package com.mllfjn.simyys.character.list.yys.yuanlaiguang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill6Passive extends Skill implements PassiveSkill {
    public static final String SkillName = "鬼胄";

    public Skill6Passive(Character belongTo, int level, int cost, int coolDown, int skillID) {
        super(belongTo, 0, 0, 0, 6);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

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
