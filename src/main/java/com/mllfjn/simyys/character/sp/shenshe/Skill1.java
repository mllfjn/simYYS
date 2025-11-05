package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill {
    public static final String SkillName = "灵魂惩戒";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 115, 125};
    public Skill1(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 1);
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

        // 对敌方目标造成(系数)伤害
        interactive.attack(SkillName, target, multiplier[getLevel()], AttackType.DAN_TI);
    }
}
