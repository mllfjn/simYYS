package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.CharacterJiFengMoBase;


public class HuangKuLou extends CharacterJiFengMoBase {
    public static final String CharacterName = "荒骷髅";

    @Override
    protected void addStage(MultiStageManager multiStageManager) {

    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(2) || tryUseSkill(3) || tryUseSkill(4) || tryUseSkill(1);
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this));
        addSkill(new Skill2(this));
        addSkill(new Skill3(this));
        addSkill(new Skill4(this));
    }

    @Override
    protected String getJiFengMoSpeed() {
        return "190";
    }

}
