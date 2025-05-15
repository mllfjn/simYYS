package com.mllfjn.simyys.character.ssr.xiaoyuan;

import com.mllfjn.simyys.character.Character;

public class XiaoYuan extends Character {
    public static final String privateName = "缘结神";
    public XiaoYuan() {

    }

    @Override
    public void initSelf(int[] skillLevels) {
        getSkills().add(new SkillPuGong(this, skillLevels[0]));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[0];
    }
}
