package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

class Skill4 extends Skill {
    private static final String SkillName = "狐影阵";
    private static final int[] multiplier = {0, 180, 195, 210, 225, 240};

    public Skill4(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 4);
    }

    @Override
    public String getSkillDesc() {
        return "造成攻击180, 195, 210, 225, 240伤害";
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();

        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy().getList();

        belongTo.getInteractive().attackTypical(this, list, multiplier[getLevel()], AttackType.QUN_TI);
        return Optional.empty();
    }
}
