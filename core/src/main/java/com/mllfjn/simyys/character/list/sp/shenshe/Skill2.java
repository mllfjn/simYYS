package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.List;
import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "神堕之力";
    private int continuousUse;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);

        // lv5-先机：释放神堕之力
        if (level >= 5) {
            Character character = getBelongTo();
            character.bp.addPriorityMove(character, () -> use(character.bp));
        }
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && !getBelongTo().isHaveStatus(StatusSheShen.class) && ((ShenShe)getBelongTo()).isZhenYa();
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {

        // 若连续释放，每次消耗鬼火递增1点
        // 用技能将其设置为2，回合结束后自动-1变为1，当为0时说明上一回合没有使用

        if (continuousUse > 0) {
            setCost(getCost() + 1);
        }
        continuousUse = 2;


        List<Character> teammates = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .filterShiShen()
                .filterSelf()
                .getList();

        // 释放化身蛇神姿态,将除自身外所有友方式神初始攻击的6%封存于1把天羽羽斩中
        double attack = 0;
        for (Character teammate : teammates) {
            StatusStoreAttack.addStack(getBelongTo(), teammate);
            attack += teammate.getInitAttack() * 0.06;
        }
        getBelongTo().addStatus(new StatusSheShen(getBelongTo(), getLevel(), attack));
        return Optional.empty();
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
