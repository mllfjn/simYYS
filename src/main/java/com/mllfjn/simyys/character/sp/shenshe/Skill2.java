package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.List;

class Skill2 extends Skill {
    public static final String privateName = "神堕之力";
    private int continuousUse;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && !getBelongTo().isHaveState(StateSheShen.privateName) && ((ShenShe)getBelongTo()).isZhenYa();
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {

        // 若连续释放，每次消耗鬼火递增1点
        // 用技能将其设置为2，回合结束后自动-1变为1，当为0时说明上一回合没有使用

        if (continuousUse > 0) {
            setCost(getCost() + 1);
        }
        continuousUse = 2;


        List<Character> teammates = CharacterFinder.findTeammateShiShen(getBelongTo(), bp.characters);
        teammates.remove(getBelongTo());

        // 释放化身蛇神姿态,将除自身外所有友方式神初始攻击的6%封存于1把天羽羽斩中
        double attack = 0;
        for (Character teammate : teammates) {
            StateStoreAttack.addStack(getBelongTo(), teammate);
            attack += teammate.getInitAttack() * 0.06;
        }
        getBelongTo().addState(new StateSheShen(getBelongTo(), getLevel(), attack));
    }

    @Override
    public void pastRound() {
        super.pastRound();
        if (continuousUse > 0) {
            continuousUse--;
            if (continuousUse == 0) {
                setCost(0);
            }
        }
    }
}
