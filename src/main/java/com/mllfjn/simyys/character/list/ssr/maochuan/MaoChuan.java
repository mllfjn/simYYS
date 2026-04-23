package com.mllfjn.simyys.character.list.ssr.maochuan;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class MaoChuan extends CharacterShiShenBase {
    public static final String CharacterName = "猫川";

    private CharacterBieGuanSiTang bieGuanSiTang;

    boolean isBieGuanSiTangExist() {
        return bieGuanSiTang != null;
    }

    void setBieGuanSiTang(CharacterBieGuanSiTang bieGuanSiTang) {
        this.bieGuanSiTang = bieGuanSiTang;
    }

    CharacterBieGuanSiTang getBieGuanSiTang() {
        return bieGuanSiTang;
    }

    @Override
    public double getLocationWhenGettingOrder() {
        if (bieGuanSiTang != null && skill3Level >= 3 && getLocation() < 70) {
            return getLocation() + 30;
        } else {
            return super.getLocationWhenGettingOrder();
        }
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
        return "3377";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill2 skill2 = new Skill2(this, skill2Level);
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level, skill2));
    }
}
