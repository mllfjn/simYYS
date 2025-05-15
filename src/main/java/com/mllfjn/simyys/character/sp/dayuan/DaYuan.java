package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;

public class DaYuan extends Character {
    public static final String privateName = "纺愿缘结神";
    public DaYuan() {

    }

    @Override
    public void initSelf(int[] skillLevels) {
        getSkills().add(new SkillPuGong(this, skillLevels[0]));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[0];
    }
    public void addShenLi(int i) {
        ShenLi shenLi = (ShenLi) this.getState(ShenLi.privateName);
        if (shenLi == null) {
            shenLi = new ShenLi(this, this);
            this.addState(shenLi);
        }
        shenLi.addCeng(i);
    }
}
