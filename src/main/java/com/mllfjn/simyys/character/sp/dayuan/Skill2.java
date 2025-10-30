package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill2 extends Skill {
    public static final String privateName = "守缘刃";

    public Skill2(Character belongTo) {
        super(belongTo, 1, 1, 0, 2);
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        // 再次释放时，解除目标【胜天之缘】
        StateCombined state = (StateCombined) getBelongTo().getState(StateCombined.privateName);
        state.from.removeState(StateSTChi.privateName);
        state.from.removeState(StateSTQing.privateName);
        state.delete();
        // 并驱散其全部减益状态与 TODO 控制效果
        state.from.dispelAllDebuff();
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && !getBelongTo().isHaveState(StateCombined.privateName);
    }
}
