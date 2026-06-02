package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.interactive.StatusSupplier;

public class StatusDiaoLing extends Status implements Displayable, AttributeModifier, StatusRunnable {
    private static final String StatusName = "凋零";

    private StatusDiaoLing(Character from, Character belongTo, int duration) {
        super(from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        setDurationType(StatusDurationType.CHI_XU, duration);
    }

    public static StatusSupplier getSupplier(int duration) {
        return new StatusSupplier(StatusName, StatusDiaoLing.class, (from, to) ->
                to.getStatus(StatusDiaoLing.class).ifPresentOrElse(
                        status -> status.setDuration(duration),
                        () -> to.addStatus(new StatusDiaoLing(from, to, duration))
                )
        );
    }

    @Override
    public String getDisplayText() {
        return StatusName + getDuration();
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return -100;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND_FIRST;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        // 回合结束时受到施加者攻击120%间接伤害
        from.doInteractive(
                interactive -> interactive.attackTypical(Skill.getInstance(StatusName), belongTo, 120, AttackType.JIAN_JIE));

        return false;
    }
}
