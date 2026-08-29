package com.mllfjn.simyys.character.list.sr.xiazhongshaonv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusShield;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.StatusSupplier;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "溢彩";
    private static final List<RandomStatus> buffs = List.of(RandomStatus.values());

    private final boolean awakening;

    private StatusAdder<?> adder;

    public Skill2(Character belongTo, boolean awakening) {
        super(belongTo, 1, 2);
        this.awakening = awakening;
        if (awakening) {
            belongTo.addStatus(new StatusAfterAttackListener(belongTo));
        }
    }

    @Override
    public void enable() {
        Character belongTo = getBelongTo();
        adder = belongTo.bp.addStatusAdder(c ->
                c.team == belongTo.team
                        ? new StatusXZBeforeRoundListener(belongTo, c)
                        : null
        );
    }

    @Override
    public void disable() {
        adder.deleteAndRemove();
    }

    @Override
    public String getName() {
        return SkillName;
    }

    private class StatusAfterAttackListener extends Status implements StatusRunnable {
        public StatusAfterAttackListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            Character attacker = ((ParamAttackInfo) param).getAttackInfo().getAttacker();
            RandomStatus choose = RateController.choose("匣中少女-溢彩:为攻击者附加减益", buffs,
                    v -> v.attribute.getText(), belongTo.bp.calc
            );
            belongTo.doInteractive(interactive -> interactive.effect(Skill2.this, attacker,
                    100, true,
                    StatusRandomStatus.getSupplier(choose)
            ));
            return false;
        }
    }

    private class StatusXZBeforeRoundListener extends Status implements StatusRunnable {

        public StatusXZBeforeRoundListener(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND || Skill2.this.awakening && trigger == Trigger.OUT_ROUND_ACTION;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            belongTo.replaceStatus(new StatusXZShield(from, belongTo, belongTo.getMaxHp() * 0.08));
            RandomStatus choose = RateController.choose("匣中少女-溢彩:获得BUFF", buffs,
                    v -> v.attribute.getText(), belongTo.bp.calc
            );
            StatusRandomStatus.install(from, belongTo, StatusType.BUFF, choose, 2);
            return false;
        }

        private static class StatusXZShield extends StatusShield {
            public StatusXZShield(Character from, Character belongTo, double shield) {
                super(from, belongTo, shield);
                setDurationType(StatusDurationType.CHI_XU, 2);
            }
        }
    }

    private static class StatusRandomStatus extends Status implements AttributeModifier {
        private final RandomStatus randomStatus;

        private StatusRandomStatus(Character from, Character belongTo, StatusType statusType,
                                   RandomStatus randomStatus, int duration
        ) {
            super(from, belongTo, statusType, StatusForm.ZHUANG_TAI);
            this.randomStatus = randomStatus;
            setDurationType(StatusDurationType.CHI_XU, duration);
        }

        static StatusSupplier getSupplier(RandomStatus randomStatus) {
            return new StatusSupplier(randomStatus.attribute.getText(), StatusRandomStatus.class,
                    (c1, c2) -> install(c1, c2, StatusType.DEBUFF, randomStatus, 1)
            );
        }

        static void install(Character from, Character belongTo, StatusType statusType,
                            RandomStatus randomStatus, int duration
        ) {
            for (Status status : belongTo.getStatuses()) {
                if (status instanceof StatusRandomStatus srs
                        && srs.randomStatus == randomStatus
                        && status.statusType == statusType
                ) {
                    status.setDuration(duration);
                    return;
                }
            }
            belongTo.addStatus(new StatusRandomStatus(from, belongTo, statusType, randomStatus, duration));

        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == randomStatus.attribute;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            double rtValue = switch (randomStatus) {
                case ATTACK -> belongTo.getInitAttack() * 0.1;
                case DEFENSE -> belongTo.getInitDefense() * 0.2;
                case CRIT_POWER, SPEED, EFFECT_RESIST_RATE -> 10;
            };
            if (statusType == StatusType.DEBUFF) {
                rtValue = -rtValue;
            }
            return rtValue;
        }
    }

    private enum RandomStatus {
        ATTACK(Attribute.ATTACK),
        DEFENSE(Attribute.DEFENCE),
        CRIT_POWER(Attribute.CRIT_POWER),
        SPEED(Attribute.SPEED),
        EFFECT_RESIST_RATE(Attribute.EFFECT_RESIST_RATE),
        ;
        private final Attribute attribute;

        RandomStatus(Attribute attribute) {
            this.attribute = attribute;
        }
    }
}
