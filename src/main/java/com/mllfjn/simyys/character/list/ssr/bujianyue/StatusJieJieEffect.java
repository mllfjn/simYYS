package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusShield;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.utils.serializable.SerialConsumer;


// TODO:该类从private static class重构为public,内部引用很乱,有空修改
public class StatusJieJieEffect extends Status {
    static final String StatusName = "山行结界";

    private final Skill3.StatusJieJieContainer statusJieJieContainer;
    Skill lastSkill;

    StatusJieJieEffect(Skill3 skill3, Character from, Character belongTo,
                       Skill3.StatusJieJieContainer statusJieJieContainer
    ) {
        super(StatusName + "效果", from, belongTo);
        this.statusJieJieContainer = statusJieJieContainer;

        if (statusJieJieContainer.increaseAttack) {
            attribute(Attribute.ATTACK, _ ->
                    Math.min(from.getInitDefense() * 0.5, belongTo.getInitAttack() * 0.2)
            );
        }
        if (statusJieJieContainer.reduceCritDamage) {
            runOn(Trigger.BEFORE_ATTACK, param -> {
                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                if (attackInfo.isCrit()) {
                    attackInfo.getTraceableNumber().mul(0.75, StatusName);
                }
            });
        }
        SerialConsumer<TriggerParam> action = param -> {
            Skill skill = ((ParamUseSkill) param).getSkill();
            if (lastSkill == null || lastSkill == skill) {
                belongTo.addStatus(getYunYi(from, belongTo));
                skill3.skill2.getYunYi();
            } else {
                runOn(Trigger.AFTER_ROUND, _ -> {
                    belongTo.addStatus(new StatusShanSe(from, belongTo));
                    skill3.skill2.getShanSe();
                    removeAction(Trigger.AFTER_ROUND);
                });
            }
            lastSkill = skill;
        };
        runOn(Trigger.WILL_USE_PU_GONG, action);
        runOn(Trigger.WILL_USE_SKILL, action);
        runOn(Trigger.WHEN_ATTACK, param -> {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (isIncreaseNonCritDamage(attackInfo)) {
                attackInfo.getTraceableNumber().mul(1.25, StatusName);
            }
        });
    }

    // TODO:这个方法为玄象奇怪的表现设立,如果后续找到解决方法需要修改
    public Status getYunYi(Character from, Character belongTo) {
        return new StatusYunYi(from, belongTo);
    }

    public boolean isIncreaseNonCritDamage(AttackInfo attackInfo) {
        return statusJieJieContainer.increaseNonCritDamage && !attackInfo.isCrit();
    }

    static class StatusYunYi extends Status {
        public StatusYunYi(Character from, Character belongTo) {
            super("云衣", from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 0);
            attribute(Attribute.ATTACK, _ ->
                    Math.min(from.getInitDefense() * 2, belongTo.getInitAttack() * 0.4)
            );
        }
    }

    static class StatusShanSe extends StatusShield {
        public StatusShanSe(Character from, Character belongTo) {
            super("山色", from, belongTo, from.getInitDefense() * 3.5);
            duration(StatusDurationType.CHI_XU, 1);
            attribute(Attribute.EFFECT_RESIST_RATE, 30.0);
        }
    }
}
