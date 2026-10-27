package com.mllfjn.simyys.character.list.ssr.kongquemingwang;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.CrowdControl;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.StatusSupplier;

public class StatusJiaoXie extends Status implements CrowdControl {
    public static final String STATUS_NAME = "缴械";

    protected StatusJiaoXie(Character from, Character belongTo, int duration) {
        super(STATUS_NAME, from, belongTo);
        belongTo.sealYuHun();
        beforeDelete(belongTo::unsealYuHun);
        beforeDispel(() ->
                Status.of("缴械驱散降伤", from, belongTo)
                        .runOn(Trigger.WHEN_ATTACK, param -> {
                            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                            if (attackInfo.getSkill() instanceof Skill1PuGongBase) {
                                attackInfo.getTraceableNumber().mul(0.8, "缴械驱散降伤");
                            }
                        })
                        .duration(StatusDurationType.CHI_XU, 1)
                        .addTo()
        );
        displayName();
        duration(StatusDurationType.CHI_XU, duration);
        addTo();
    }

    static StatusSupplier getSupplier(int duration) {
        return new StatusSupplier(STATUS_NAME, StatusJiaoXie.class, (from, belongTo) -> {
            if (belongTo.getStatus(StatusJiaoXie.class).isEmpty()) {
                new StatusJiaoXie(from, belongTo, duration);
            }
        });
    }
}
