package com.mllfjn.simyys.character.yys.shenle;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

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
    public void usePrivate(BattlePane bp) {
        Character target = CharacterFinder.findPriorAuto(bp, CharacterFinder.getEnemyTeam(getBelongTo()), CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);
        lastUsedTarget = target;

        Interactive interactive = getBelongTo().getInteractive();
        interactive.attack(SkillName, target, multiplier[getLevel()] + 20 * shuYin, AttackType.DAN_TI);
    }
}
