package com.mllfjn.simyys.character.list.sr.rihefang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "滋养";
    private static final int[] averageHeal = new int[]{0, 50, 75, 75, 100, 100};

    private final Skill2 skill2;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 2, 0, 3);
        this.skill2 = skill2;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        skill2.useSkill3();
        return Optional.empty();
    }

    double getUseStore() {
        return getBelongTo().getMaxHp() * (getLevel() >= 3 ? 0.4 : 0.25);
    }

    double getAverageHeal() {
        return 0.01 * averageHeal[getLevel()];
    }

    int getReviveRound() {
        return getLevel() >= 5 ? 3 : 4;
    }
}
