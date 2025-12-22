package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill1 extends Skill1PuGongBase {
    public static final String SkillName = "纺缘";
    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(BattlePane bp, Interactive interactive, Character target) {
        // 用久违的神力攻击敌方目标,造成攻击(系数)的伤害
        interactive.attackTypical(this, target, multiplierGeneral[getLevel()], AttackType.DAN_TI);
        // lv5-获得1层神力
        if (getLevel() >= 5) {
            StatusShenLi.addStack(getBelongTo(), 1);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
