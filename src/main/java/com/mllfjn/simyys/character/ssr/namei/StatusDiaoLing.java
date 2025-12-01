package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.trigger.Trigger;

public class StatusDiaoLing extends Status implements Displayable, AttributeModifier, Runnable {
    public static final String text = "凋零";

    public StatusDiaoLing(Character from, Character belongTo, int duration) {
        super(from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        setSettleType(StatusDurationType.CHI_XU, duration);
    }

    @Override
    public String getText() {
        return text + getDuration();
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return -100;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND_FIRST;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp) {
        Interactive interactive = from.getInteractive();
        // 回合结束时受到施加者攻击120%间接伤害
        interactive.attack(text, belongTo, 120, AttackType.JIAN_JIE);

        return false;
    }
}
