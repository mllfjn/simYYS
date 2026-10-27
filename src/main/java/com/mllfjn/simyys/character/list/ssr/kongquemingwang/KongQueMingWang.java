package com.mllfjn.simyys.character.list.ssr.kongquemingwang;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class KongQueMingWang extends CharacterShiShenBase {
    public static final String CharacterName = "孔雀明王";

    private StatusKongQueLing statusKongQueLing;

    private Skill1 skill1;
    private Skill3 skill3;

    private Skill1Special skill1Special;
    private Skill3Special skill3Special;

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }

    void setStatusKongQueLing(StatusKongQueLing statusKongQueLing) {
        this.statusKongQueLing = statusKongQueLing;
    }

    void addKongQueLing() {
        statusKongQueLing.addStack();
    }

    void kaiPing() {
        removeSkill(skill1);
        removeSkill(skill3);

        if (skill1Special == null) {
            skill1Special = new Skill1Special(this, skill1Level);
            skill3Special = new Skill3Special(this, skill3Level);
        }

        addSkill(skill1Special, true);
        addSkill(skill3Special, true);
    }

    void stopKaiPing() {
        removeSkill(skill1Special);
        removeSkill(skill3Special);

        addSkill(skill1, true);
        addSkill(skill3, true);
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
        return "3538";
    }

    @Override
    protected void addOwnSkills() {
        skill1 = new Skill1(this, skill1Level);
        skill3 = new Skill3(this, skill3Level);

        addSkill(skill1);
        addSkill(new Skill2(this, skill2Level));
        addSkill(skill3);
    }
}
