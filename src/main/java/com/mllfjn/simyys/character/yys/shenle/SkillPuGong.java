package com.mllfjn.simyys.character.yys.shenle;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.hit.AttackType;
import com.mllfjn.simyys.hit.Hit;

class SkillPuGong extends Skill {
    public static final String privateName = "伞击";
    private static final int[] multiplier = new int[]{0, 100, 110, 120, 130, 140};
    public SkillPuGong(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character target = CharacterFinder.findPriorAuto(bp, CharacterFinder.findEnemy(getBelongTo()), CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);
        lastUsedTarget = target.name;

        Hit hit = new Hit(bp, getBelongTo());
        hit.attack(privateName, target, multiplier[getLevel()], AttackType.DAN_TI);
    }

    @Override
    public void setName() {
        this.name = privateName;
    }
}
