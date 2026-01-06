package com.mllfjn.simyys.character.list.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.character.status.Trigger;

import java.util.List;
import java.util.Optional;

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
    public Optional<Character> usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive();
        // 对敌方全体造成攻击(120 + 悲歌层数*100)%的伤害
        List<Character> targets = new CharacterFinder(getBelongTo()).setTargetTeam(CharacterFinder.TargetTeam.ENEMY).getList();
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

class StatusBeiGe extends Status implements StatusRunnable, Displayable {
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
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        // 千姬回合开始时获得1层悲歌(没写,但是上限应该是5层)
        if (stack < 5) {
            stack++;
        }
        return false;
    }

    @Override
    public String getDisplayText() {
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
