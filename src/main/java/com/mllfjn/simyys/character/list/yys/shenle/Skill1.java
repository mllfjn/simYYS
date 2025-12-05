package com.mllfjn.simyys.character.list.yys.shenle;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.Optional;

class Skill1 extends Skill {
    public static final String SkillName = "伞击";
    private static final int[] multiplier = new int[]{0, 100, 110, 120, 130, 140};

    private final int shuYin;

    public Skill1(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 1);
        this.shuYin = shuYin;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character target = new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getPriorAuto(CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);

        getBelongTo().getInteractive().attack(SkillName, target, multiplier[getLevel()] + 20 * shuYin, AttackType.DAN_TI);

        return Optional.of(target);
    }
}
