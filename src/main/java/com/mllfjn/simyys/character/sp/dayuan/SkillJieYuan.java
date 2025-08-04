package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.state.State;

import java.util.List;

abstract class SkillJieYuan extends Skill {
    public SkillJieYuan(Character belongTo) {
        super(belongTo, 1, 1, 0);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && getBelongTo().getState(StateFlagCombined.privateName) == null && getTarget(bp) != null;
    }

    public Character getTarget(BattlePane bp) {
        List<Character> list = CharacterFinder.findTeammate(getBelongTo(), bp.characters);
        list.remove(getBelongTo());
        return CharacterFinder.findPriorAuto(list, bp, getBelongTo().team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character target = getTarget(bp);
        lastUsedTarget = target;
        jieYuan(target);
        getBelongTo().addState(new StateFlagCombined(target, getBelongTo()));
        StateShenLi.addStack(getBelongTo(), 2);
    }

    abstract void jieYuan(Character target);
}
