package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

public class HaiYuanBeiJi extends Character {
    QianJi qianJi;
    public HaiYuanBeiJi(QianJi qianJi) {
        this.qianJi = qianJi;
    }
    @Override
    public void initSelf(int[] skillLevels) {

    }

    @Override
    public int[] getUseSkillOrder() {
        return null;
    }

    public static void addStack(BattlePane bp, int count) {

    }

    @Override
    public void beForeDie(BattlePane bp) {
        super.beForeDie(bp);
        qianJi.setHavePutDown(false);
    }
}
