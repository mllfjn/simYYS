package com.mllfjn.simyys.character.list.sr.huajing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    static final String SkillName = "体甲";
    static final int[] multiplier = new int[]{0, 6, 7, 8, 9, 10};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 3);
        if (level < 5) {
            setCost(3);
        }
    }

    int getMultiplier() {
        return multiplier[getLevel()];
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        HuaJing belongTo = ((HuaJing) getBelongTo());
        Character target = new CharacterFinder(belongTo)
                .filterTeammate()
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        StatusTiJia statusTiJia = new StatusTiJia(belongTo, target, this);
        target.replaceStatus(statusTiJia);
        belongTo.statusTiJia = statusTiJia;

        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();
        belongTo.getInteractive().healTypical(this, list, multiplier[getLevel()]);

        return Optional.of(target);
    }
}
