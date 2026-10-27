package com.mllfjn.simyys.character.list.sp.huishihuaniaojuan;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class HuiShiHuaNiaoJuan extends CharacterShiShenBase {
    public static final String CHARACTER_NAME = "绘世花鸟卷";

    private Skill2 skill2;

    private Skill3 skill3;
    private Skill3Special skill3Special;

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }

    void replaceSkill3Special(Skill3.CharacterFuShengHuaYing.StatusZhuMo statusZhuMo) {
        removeSkill(skill3);
        if (skill3Special == null) {
            skill3Special = new Skill3Special(this, skill3Level);
        }
        skill3Special.statusZhuMo = statusZhuMo;
        addSkill(skill3Special, true);
    }

    void backToNormal() {
        removeSkill(skill3Special);
        addSkill(skill3, true);

        if (skill2Level >= 5 && !isInRound() && skill2.isActive()) {
            doInteractive(skill2, interactive -> interactive.increaseLocation(this, 30));
        }
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return false;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2278";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        skill2 = new Skill2(this, skill2Level);
        addSkill(skill2);
        skill3 = new Skill3(this, skill3Level);
        addSkill(skill3);
    }
}
