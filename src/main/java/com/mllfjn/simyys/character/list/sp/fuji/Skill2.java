package com.mllfjn.simyys.character.list.sp.fuji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "蚀骨";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character target = new CharacterFinder(getBelongTo())
                .filterEnemy()
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
        return Optional.of(target);
    }
}
