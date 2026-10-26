package com.mllfjn.simyys.character.list.ssr.xiaoye;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;

public class StatusChaNa extends Status {
    StatusChaNa(Character from, Character belongTo, Interactive interactive, double increasePerPoint, boolean extraPuGong) {
        super("刹那", from, belongTo);
        interactive.getNewRound(belongTo);
        belongTo.chaNa(this);
        runOn(Trigger.WHEN_ATTACK, param -> {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (attackInfo.getSkill() instanceof Skill1PuGongBase) {
                attackInfo.getTraceableNumber()
                        .mul(1 + increasePerPoint * (int) (from.getInitSpeed() / 15), getName());
            }
        });
        if (extraPuGong) {
            runOn(Trigger.USED_PU_GONG, param -> {
                ParamUseSkill pus = (ParamUseSkill) param;
                Skill1PuGongBase s1pg = (Skill1PuGongBase) pus.getSkill();
                Character target = pus.getTarget().orElseThrow();
                belongTo.bp().addOutRoundSkill(s1pg, () -> {
                    belongTo.doInteractive(s1pg, i1 ->
                            s1pg.usePrivate(i1, target)
                    );
                    s1pg.useOver(target);
                });
            });
        }
        addTo();
    }
}
