package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.instance.StatusShield;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;


// TODO:该类从private static class重构为public,内部引用很乱,有空修改
public class StatusJieJieEffect extends Status implements AttributeModifier, StatusRunnable, InfluenceDamageWhenAttack {
    private static final String StatusName = "山行结界";

    private final Skill3 skill3;
    private final Skill3.StatusJieJieContainer statusJieJieContainer;
    Skill lastSkill;
    private boolean getShanSeAfterRound;

    StatusJieJieEffect(Skill3 skill3, Skill3.StatusJieJieContainer statusJieJieContainer, Character from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.skill3 = skill3;
        this.statusJieJieContainer = statusJieJieContainer;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return statusJieJieContainer.increaseAttack && attribute == Attribute.ATTACK;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return Math.min(from.getInitDefense() * 0.5, belongTo.getInitAttack() * 0.2);
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return getShanSeAfterRound
                || (trigger == Trigger.WILL_USE_PU_GONG || trigger == Trigger.WILL_USE_SKILL)
                || (statusJieJieContainer.reduceCritDamage && trigger == Trigger.BEFORE_ATTACK);
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.BEFORE_ATTACK) {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (attackInfo.isCrit()) {
                attackInfo.getTraceableNumber().mul(0.75, StatusName);
            }
        } else if (getShanSeAfterRound && trigger == Trigger.AFTER_ROUND) {
            belongTo.addStatus(new StatusShanSe(from, belongTo));
            skill3.skill2.getShanSe();
            getShanSeAfterRound = false;
        } else {
            Skill skill = ((ParamUseSkill) param).getSkill();
            if (lastSkill == null || lastSkill == skill) {
                belongTo.addStatus(getYunYi(from, belongTo));
                skill3.skill2.getYunYi();
            } else {
                getShanSeAfterRound = true;
            }
            lastSkill = skill;
        }
        return false;
    }

    // TODO:这个方法为玄象奇怪的表现设立,如果后续找到解决方法需要修改
    public Status getYunYi(Character from, Character belongTo) {
        return new StatusYunYi(from, belongTo);
    }

    @Override
    public void doInfluenceWhenAttack(AttackInfo attackInfo) {
        if (isCreaseNonCritDamage(attackInfo)) {
            attackInfo.getTraceableNumber().mul(1.25, StatusName);
        }
    }

    public boolean isCreaseNonCritDamage(AttackInfo attackInfo) {
        return statusJieJieContainer.increaseNonCritDamage && !attackInfo.isCrit();
    }

    static class StatusYunYi extends Status implements AttributeModifier {
        public StatusYunYi(Character from, Character belongTo) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 0);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return Math.min(from.getInitDefense() * 2, belongTo.getInitAttack() * 0.4);
        }
    }

    static class StatusShanSe extends StatusShield implements Displayable, AttributeModifier {
        public StatusShanSe(Character from, Character belongTo) {
            super(from, belongTo, from.getInitDefense() * 3.5);
            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.EFFECT_RESIST_RATE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 30;
        }

        @Override
        public String getDisplayText() {
            return "山色";
        }
    }
}
