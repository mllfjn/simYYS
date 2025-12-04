package com.mllfjn.simyys.character.yys.yuanlaiguang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.ratecontroller.RateController;

class Skill1 extends Skill {
    public static final String SkillName = "天剑";
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
        // 对敌方目标造成攻击(系数)伤害
        Character belongTo = getBelongTo();
        belongTo.getInteractive().attack(SkillName, RateController.choose())
        CharacterFinder.findEnemy(belongTo, bp.situation.characters);
    }
}
