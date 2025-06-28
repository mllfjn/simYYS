package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.character.Character;

public class QianJi extends Character {
    public static final String privateName = "千姬";
    private boolean havePutDown = false;

    @Override
    public void initSelf(int[] skillLevels) {
        getSkills().put(1, new Skill1TODO(this, skillLevels[0]));
        getSkills().put(3, new Skill3PutTODO(this, skillLevels[2]));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[0];
    }

    public boolean isHavePutDown() {
        return havePutDown;
    }

    public void setHavePutDown(boolean havePutDown) {
        this.havePutDown = havePutDown;
    }
}
