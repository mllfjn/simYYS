package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill3PutTODO extends Skill {
    public static final String privateName = "海潮入梦";

    public Skill3PutTODO(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 3);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && !((QianJi) getBelongTo()).isHavePutDown() && bp.canSummon(getBelongTo().team);
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        ((QianJi) getBelongTo()).setHavePutDown(true);
    }
}
