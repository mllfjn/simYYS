package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.List;

abstract class SkillJieYuan extends Skill {
    public SkillJieYuan(Character belongTo, int skillID) {
        super(belongTo, 1, 1, 0, skillID);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && getTarget(bp) != null;
    }

    public Character getTarget(BattlePane bp) {
        List<Character> list = CharacterFinder.findTeammate(getBelongTo(), bp.situation.characters);
        list.remove(getBelongTo());
        return CharacterFinder.findPriorAuto(list, bp, getBelongTo().team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character target = getTarget(bp);
        lastUsedTarget = target;
        // 为自身以外的指定友方目标缔结胜天之缘·赤(Skill5)或胜天之缘·青(Skill6)
        jieYuan(target);
        // 并提升自身2层神力
        StateShenLi.addStack(getBelongTo(), 2);
        // 释放后删除结缘技能
        getBelongTo().removeSkill(5);
        getBelongTo().removeSkill(6);
        // 释放后可以获得解除结缘的技能二
        getBelongTo().addSkill(new Skill2(getBelongTo()));
    }

    abstract void jieYuan(Character target);
}