package com.mllfjn.simyys.character.list.ssr.xiaoye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "刹那之花";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, level >= 2 ? 2 : 3, 0, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        int level = getLevel();
        Character target = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .filterSelf()
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        new StatusChaNa(getBelongTo(), target,
                level >= 4 ? 0.02 : level == 3 ? 0.015 : 0.01,
                level >= 5
        );

        return Optional.of(target);
    }
}
