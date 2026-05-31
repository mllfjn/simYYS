package com.mllfjn.simyys.character.list.sr.qingji;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class QingJi extends CharacterShiShenBase {
    public static final String CharacterName = "清姬";

    @Override
    protected String getDefaultSkillLevel() {
        return "615";
    }

    @Override
    protected boolean canAwakening() {
        return true;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2412";
    }

    @Override
    protected void addOwnSkills() {

    }
}
