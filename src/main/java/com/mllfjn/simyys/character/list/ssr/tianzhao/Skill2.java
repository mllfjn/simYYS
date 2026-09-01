package com.mllfjn.simyys.character.list.ssr.tianzhao;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.ArrayList;
import java.util.List;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "无灭";
    private static final double[] Coefficients = new double[]{0, 0.55, 0.6, 0.65, 0.7, 0.7};

    private final List<Copy> copyList = new ArrayList<>();
    private StatusGDListener status;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
    }

    @Override
    public String getSkillDesc() {
        return "天辉复制伤害除了复制的目标的增伤还有什么来着?";
    }

    void causeAttack(AttackInfo attackInfo) {
        copyList.add(new Copy(attackInfo.getTarget(), attackInfo.getMultiplier(), attackInfo.getAttackType()));
    }

    void copyDone(Character copyTarget) {
        if (isActive() && !copyList.isEmpty()) {
            TianZhao tianZhao = (TianZhao) getBelongTo();
            if (!tianZhao.isUncontrollable()) {
                tianZhao.bp.addOutRoundSkill(this, () -> {
                    tianZhao.setCopyTarget(copyTarget);
                    double coefficient = Coefficients[getLevel()];
                    tianZhao.doInteractive(interactive ->
                            copyList.forEach(copy ->
                                    interactive.attackTypical(this, copy.target,
                                            copy.multiplier * coefficient,
                                            copy.attackType
                                    )
                            )
                    );
                    tianZhao.setCopyTarget(null);
                    useDone();
                });
            }
        }
        copyList.clear();
    }

    @Override
    public void enable() {
        if (status == null) {
            status = new StatusGDListener(getBelongTo());
        }
        getBelongTo().addStatus(status);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(status);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    private record Copy(Character target, double multiplier, AttackType attackType) {
    }

    private static class StatusGDListener extends Status {
        public StatusGDListener(Character character) {
            super(StatusGuangDi.StatusName + "伤害监听", character);
            runOn(Trigger.BEING_ATTACKED, param -> {
                Character attacker = ((ParamAttackInfo) param).getAttackInfo().getAttacker();
                if (attacker.team != belongTo.team) {
                    StatusGuangDi.install(belongTo, attacker);
                }
            });
        }
    }

    private static class StatusGuangDi extends Status {
        private static final String StatusName = "光涤";

        private StatusGuangDi(Character from, Character belongTo) {
            super(StatusName, from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
            duration(StatusDurationType.CHI_XU, 1);
            displayName();
            runOn(Trigger.WHEN_ATTACK, param -> {
                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                if (attackInfo.getTarget() == from) {
                    attackInfo.getTraceableNumber().mul(0.6, StatusName);
                }
            });
            attribute(Attribute.DEFENCE, -150);
        }

        private static void install(Character from, Character belongTo) {
            belongTo.getStatus(StatusGuangDi.class)
                    .ifPresentOrElse(
                            status -> status.duration(1),
                            () -> belongTo.addStatus(new StatusGuangDi(from, belongTo))
                    );
        }
    }
}
