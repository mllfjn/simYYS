package com.mllfjn.simyys.character.list.ssr.axiuluo;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class AXiuLuo extends CharacterShiShenBase {
    public static final String CharacterName = "阿修罗";

    StatusLiXing statusLiXing;

    SkillWuJianShaLu skillWuJianShaLu;

    @Override
    public void beforeRound() {
        if (skillWuJianShaLu != null) {
            skillWuJianShaLu.beforeRound();
        }
        super.beforeRound();
    }

    @Override
    public void round() {
        if (skillWuJianShaLu == null) {
            super.round();
        }
    }

    @Override
    public boolean isUncontrollable() {
        return skillWuJianShaLu != null;
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
        return "4127";
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }

    @Override
    protected void addOwnSkills() {
        Skill2 skill2 = new Skill2(this, skill2Level);
        statusLiXing = skill2.getStatusLiXing(this);
        addStatus(statusLiXing);

        addSkill(new Skill1(this, skill1Level));
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level));

        addStatus(new StatusKilledListener(this, skill2));
    }
}
