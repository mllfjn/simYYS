package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill1 extends Skill1PuGongBase {
    public static final String SkillName = "灵魂惩戒";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 115, 125};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(BattlePane bp, Interactive interactive, Character target) {
        // 对敌方目标造成(系数)伤害
        interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
