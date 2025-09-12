package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import javafx.collections.ObservableList;

public class HaiYuanBeiJi extends Character {
    QianJi qianJi;
    public HaiYuanBeiJi(QianJi qianJi) {
        this.qianJi = qianJi;
    }
    @Override
    public void addSkill(ObservableList<Skill> skills) {

    }

    public static void addStack(BattlePane bp, int count) {
//TODO
    }

    @Override
    public void beForeDie(BattlePane bp) {
        super.beForeDie(bp);
        qianJi.setHavePutDown(false);
    }
}
