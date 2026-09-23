package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.interactive.StatusSupplier;

public class StatusDiaoLing extends Status {
    private static final String StatusName = "凋零";

    private StatusDiaoLing(Character from, Character belongTo, int duration) {
        super(StatusName, from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        duration(StatusDurationType.CHI_XU, duration);
        displayNameAndDuration();
        attribute(Attribute.DEFENCE, -100.0);
        Skill skill = Skill.getInstance(StatusName);
        // 回合结束时受到施加者攻击120%间接伤害
        runOn(Trigger.AFTER_ROUND_FIRST, _ -> {
            from.doInteractive(
                    interactive ->
                            interactive.attackTypical(skill, belongTo, 120, AttackType.JIAN_JIE)
            );
            skill.useDone();
        });
    }

    public static StatusSupplier getSupplier(int duration) {
        return new StatusSupplier(StatusName, StatusDiaoLing.class, (from, to) ->
                to.getStatus(StatusDiaoLing.class).ifPresentOrElse(
                        status -> status.duration(duration),
                        () -> to.addStatus(new StatusDiaoLing(from, to, duration))
                )
        );
    }
}
