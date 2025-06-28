package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.character.Character;

public class HaiYuanBeiJi extends Character {
    private final QianJi belongTo;
    public HaiYuanBeiJi(QianJi belongTo) {
        super();
        this.belongTo = belongTo;
    }
    @Override
    public void initSelf(int[] skillLevels) {

    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[0];
    }

    @Override
    public void beForeDie() {
        super.beForeDie();

    }
}
