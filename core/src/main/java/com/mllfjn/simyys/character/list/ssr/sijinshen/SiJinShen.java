package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class SiJinShen extends CharacterShiShenBase {
    public static final String CharacterName = "思金神";

    boolean getNewRoundWhenXinYangRemoved = false;
    StatusXinYang statusXinYang;
    SkillQiMeng qm;

    void addXinYangStack() {
        if (statusXinYang != null) {
            statusXinYang.addStack();
        } else {
            statusXinYang = new StatusXinYang(this, getNewRoundWhenXinYangRemoved);
            addStatus(statusXinYang);
        }
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return true;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2144";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill2 skill2 = new Skill2(this, skill2Level);
        addSkill(skill2);
        qm = new SkillQiMeng(this, skill2.getCoefficient());
        addSkill(new Skill3(this, skill3Level));
    }
}
