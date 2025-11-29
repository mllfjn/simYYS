package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.status.*;
import com.mllfjn.simyys.status.Runnable;
import com.mllfjn.simyys.trigger.Trigger;

class Skill3_2 extends Skill {
    public static final String SkillName = "永生之汐";

    public Skill3_2(Character belongTo) {
        super(belongTo, 0, 2, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive();
        // 对敌方全体造成攻击(120 + 悲歌层数*100)%的伤害
        interactive.attack(SkillName, CharacterFinder.findEnemy(getBelongTo(), bp.situation.characters)
                , 120 + getBelongTo().getStatus(StatusBeiGe.class).orElseThrow().getStack() * 100
                , AttackType.QUN_TI);
        // 释放后移除悲歌,并将技能替换为海潮入梦(3-1)
        QianJi qianJi = (QianJi) getBelongTo();
        removeBeiGeAndChangeSkill(qianJi);
        qianJi.getHaiYuanBeiJi().die();
    }

    public static void removeBeiGeAndChangeSkill(QianJi qianJi) {
        qianJi.removeStatus(StatusBeiGe.class);
        qianJi.getSkill3_1();
    }
}

class StatusBeiGe extends Status implements Runnable, Displayable {
    private static final String StatusName = "悲歌";
    private int stack;

    public StatusBeiGe(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp) {
        // 千姬回合开始时获得1层悲歌(没写,但是上限应该是5层)
        if (stack < 5) {
            stack++;
        }
        return false;
    }

    @Override
    public String getText() {
        if (stack == 0) {
            return null;
        } else {
            return StatusName + stack;
        }
    }

    public int getStack() {
        return stack;
    }
}
