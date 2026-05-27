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
        return tryUseSkill(2) // 毒雾
                || tryUseSkill(3) // 刀剑
                || tryUseSkill(4) // 花海
                || tryUseSkill(6); // 刀锋
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill6(this));
        addSkill(new Skill2(this));
        addSkill(new Skill3(this));
        addSkill(new Skill4(this));
    }

    @Override
    protected String getJiFengMoSpeed() {
        return "190";
    }

}
