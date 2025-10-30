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
        return super.canUse(bp)
//                && !getBelongTo().isHaveState(StateCombined.privateName) // 场上至多存在1个目标处于胜天之缘(无用,因为当前逻辑中有目标处于胜天之缘时,无法释放结缘技能)
                && getTarget(bp) != null;
    }

    public Character getTarget(BattlePane bp) {
        List<Character> list = CharacterFinder.findTeammate(getBelongTo(), bp.characters);
        // 为自身以外的
        list.remove(getBelongTo());
        return CharacterFinder.findPriorAuto(list, bp, getBelongTo().team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character target = getTarget(bp);
        lastUsedTarget = target;
        // 的指定友方目标缔结胜天之缘·赤(Skill5)或胜天之缘·青(Skill6)
        jieYuan(target);
        // 并提升自身2层神力
        StateShenLi.addStack(getBelongTo(), 2);
        // 释放后可以获得解除结缘的技能二
        getBelongTo().getSkills().add(new Skill2(getBelongTo()));
    }

    abstract void jieYuan(Character target);
}