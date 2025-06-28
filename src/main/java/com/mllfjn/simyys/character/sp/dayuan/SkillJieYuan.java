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
        State chi = getBelongTo().getState(StateSTChiTODO.privateName);
        State qing = getBelongTo().getState(StateSTQingTODO.privateName);
        return super.canUse(bp) && chi == null && qing == null && getTarget(bp) != null;
    }

    public Character getTarget(BattlePane bp) {
        List<Character> list = CharacterFinder.findTeammate(getBelongTo(), bp.characters);
        return CharacterFinder.findPriorAuto(list, bp.autoTo[getBelongTo().team], CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
    }
}
