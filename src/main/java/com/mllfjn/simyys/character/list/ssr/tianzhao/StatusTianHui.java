package com.mllfjn.simyys.character.list.ssr.tianzhao;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusTianHui extends Status implements AttributeModifier, Displayable, StatusRunnable, PreventDie {
    private static final String StatusName = "天辉";

    private final Skill2 skill2;
    private final Skill3 skill3;

    private Skill copySkill;

    StatusTianHui(Character from, Character belongTo, Skill2 skill2, Skill3 skill3) {
        super(from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        this.skill2 = skill2;
        this.skill3 = skill3;

        setDurationType(StatusDurationType.WEI_CHI, 2);
        skill3.statusTianHui = this;
    }

    @Override
    public void beforeDelete() {
        skill3.statusTianHui = null;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.ATTACK;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return -belongTo.getInitAttack() * 0.15;
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return (
                belongTo.isInRound() // 在自身回合释放普攻或技能
                        && (trigger == Trigger.WILL_USE_PU_GONG || trigger == Trigger.WILL_USE_SKILL))
                || (
                copySkill != null
                        && (trigger == Trigger.CAUSE_ATTACK // 释放的技能造成了伤害
                        || trigger == Trigger.USED_PU_GONG || trigger == Trigger.USED_SKILL)); // 技能结束
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.WILL_USE_PU_GONG || trigger == Trigger.WILL_USE_SKILL) {
            copySkill = ((ParamUseSkill) param).getSkill();
        } else if (trigger == Trigger.CAUSE_ATTACK) {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (attackInfo.getSkill() == copySkill) {
                skill2.causeAttack(attackInfo);
            }
        } else {
            copySkill = null;
            skill2.copyDone(belongTo);
        }
        return false;
    }

    @Override
    public void preventDie(double excessDamage) {
        from.lostHP(1);
    }

    @Override
    public String getName() {
        return StatusName;
    }
}
