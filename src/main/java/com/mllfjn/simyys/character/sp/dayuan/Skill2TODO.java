package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill2TODO extends Skill {
    public static final String privateName = "守缘刃";

    public Skill2TODO(Character belongTo) {
        super(belongTo, 1, 1, 0);
    }

    @Override
    public int getSkillID() {
        return 2;
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        StateCombined state = (StateCombined) getBelongTo().getState(StateCombined.privateName);
        state.from.deleteState(StateSTChiTODO.privateName);
        state.from.deleteState(StateSTQingTODO.privateName);
        state.delete();
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && getBelongTo().getState(StateCombined.privateName) == null;
    }
}
