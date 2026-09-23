package com.mllfjn.simyys.character.list.sp.fuji;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.character.status.instance.StatusPoisoning;

public class FuJi extends CharacterShiShenBase {
    public static final String CharacterName = "缚骨清姬";


    private int defensePerStack;
    private int poisonLevel;

    void attack(Character target) {
        StatusFJReduceDefense.addStack(this, target, defensePerStack);
        StatusPoisoning.add(this, target, poisonLevel, 5);
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
        return "3028";
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(2) || tryUseSkill(3);
    }

    @Override
    protected void addOwnSkills() {
        defensePerStack = skill1Level >= 5 ? 50 : 20;
        poisonLevel = skill1Level >= 5 ? 5 : 3;
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
