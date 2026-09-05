package com.mllfjn.simyys.character.list.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.character.status.Trigger;

import java.util.List;
import java.util.Optional;

class Skill3_2 extends Skill {
    private static final String SkillName = "永生之汐";

    public Skill3_2(Character belongTo) {
        super(belongTo, 0, 2, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive();
        // 对敌方全体造成攻击(120 + 悲歌层数*100)%的伤害
        List<Character> targets = new CharacterFinder(getBelongTo())
                .filterEnemy()
                .getList();
        interactive.attackTypical(this, targets
                , 120 + getBelongTo().getStatus(StatusBeiGe.class).orElseThrow().getStack() * 100
                , AttackType.QUN_TI);
        // 释放后移除悲歌,并将技能替换为海潮入梦(3-1)
        QianJi qianJi = (QianJi) getBelongTo();
        removeBeiGeAndChangeSkill(qianJi);
        qianJi.getHaiYuanBeiJi().die();
        return Optional.empty();
    }

    public static void removeBeiGeAndChangeSkill(QianJi qianJi) {
        qianJi.removeStatus(StatusBeiGe.class);
        qianJi.getSkill3_1();
    }
}

class StatusBeiGe extends Status {
    private static final String StatusName = "悲歌";
    private int stack;

    public StatusBeiGe(Character character) {
        super(StatusName, character);
        // 千姬回合开始时获得1层悲歌(没写,但是上限应该是5层)
        runOn(Trigger.BEFORE_ROUND, _ -> {
            stack++;

            if (stack == 1) {
                display(() -> StatusName + stack);
            } else if (stack == 5) {
                removeAction(Trigger.BEFORE_ROUND);
            }
        });
    }

    public int getStack() {
        return stack;
    }
}
