package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

public class HaiYuanBeiJi extends Character {
    QianJi qianJi;
    public HaiYuanBeiJi(QianJi qianJi) {
        this.qianJi = qianJi;
    }

    public static void addStack(BattlePane bp, int count) {
//TODO
    }

    @Override
    public void die(BattlePane bp) {
        super.die(bp);
        qianJi.setHavePutDown(false);
    }
}
