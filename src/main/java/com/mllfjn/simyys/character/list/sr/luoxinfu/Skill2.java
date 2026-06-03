package com.mllfjn.simyys.character.list.sr.luoxinfu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.StatusSupplier;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "蜘蛛印记";

    Skill2(Character belongTo) {
        super(belongTo, 1, 2);
    }

    void madeAttack(Character target) {
        if (isActive()) {
            getBelongTo().doInteractive(interactive ->
                    interactive.effect(this, target, 60, 0, true,
                            new StatusSupplier(StatusZhuYin.StatusName, StatusZhuYin.class,
                                    (from, to) ->
                                            to.getStatus(StatusZhuYin.class)
                                                    .ifPresentOrElse(
                                                            status -> status.setDuration(2),
                                                            () -> to.addStatus(new StatusZhuYin(getBelongTo(), to))
                                                    )
                            )
                    )
            );
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    private class StatusZhuYin extends Status implements StatusRunnable {
        private static final String StatusName = "蛛印";

        private StatusZhuYin(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.USED_SKILL;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamUseSkill pus && pus.getCost() > 0) {
                from.doInteractive(interactive ->
                        interactive.attack(AttackInfo
                                .createJianJieAttack(from, Skill2.this, belongTo, from.getAttack())
                        )
                );
                belongTo.addStatus(new StatusZhuYin.StatusReduceSpeed(from, belongTo));
            }
            return false;
        }

        static class StatusReduceSpeed extends Status implements AttributeModifier {
            public StatusReduceSpeed(Character from, Character belongTo) {
                super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
                setDurationType(StatusDurationType.CHI_XU, 1);
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return attribute == Attribute.SPEED;
            }

            @Override
            public double getInfluence(Attribute attribute, StatusModifyParam param) {
                return -20;
            }
        }
    }
}
