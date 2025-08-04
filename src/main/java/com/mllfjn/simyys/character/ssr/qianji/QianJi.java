package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import javafx.collections.ObservableList;

public class QianJi extends Character {
    public static final String privateName = "千姬";
    private boolean havePutDown = false;

    @Override
    public void initSelf(int[] skillLevels) {
        ObservableList<Skill> skills = getSkills();
        skills.add(new Skill1TODO(this, skillLevels[0]));
        skills.add(new Skill3PutTODO(this, skillLevels[2]));
    }

    @Override
    public int[] getUseSkillOrder() {
        return havePutDown ? null : new int[]{3};
    }

    public boolean isHavePutDown() {
        return havePutDown;
    }

    public void setHavePutDown(boolean havePutDown) {
        this.havePutDown = havePutDown;
    }
}
