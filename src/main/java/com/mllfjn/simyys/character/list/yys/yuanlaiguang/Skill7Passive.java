package com.mllfjn.simyys.character.list.yys.yuanlaiguang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill7Passive extends Skill implements PassiveSkill {
    public static final String SkillName = "剑之垒";

    public Skill7Passive(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 7);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }
}
