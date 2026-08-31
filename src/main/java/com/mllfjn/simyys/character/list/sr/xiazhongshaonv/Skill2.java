package com.mllfjn.simyys.character.list.sr.xiazhongshaonv;

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
import com.mllfjn.simyys.utils.serializable.SerialConsumer;

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
            Status.of(SkillName + "受到攻击监听", belongTo)
                    .runOn(Trigger.AFTER_ATTACK, param -> {
                        Character attacker = ((ParamAttackInfo) param).getAttackInfo().getAttacker();
                        RandomStatus choose = RateController.choose("匣中少女-溢彩:为攻击者附加减益", buffs,
                                v -> v.attribute.getText(), belongTo.bp.calc
                        );
                        belongTo.doInteractive(interactive -> interactive.effect(Skill2.this, attacker,
                                100, true,
                                StatusRandomStatus.getSupplier(choose)
                        ));
                    }).addTo();
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

    private class StatusXZBeforeRoundListener extends Status {

        public StatusXZBeforeRoundListener(Character from, Character belongTo) {
            super(SkillName + "回合开始监听", from, belongTo);
            SerialConsumer<TriggerParam> action = _ -> {
                belongTo.replaceStatus(new StatusXZShield(from, belongTo, belongTo.getMaxHp() * 0.08));
                RandomStatus choose = RateController.choose("匣中少女-溢彩:获得BUFF", buffs,
                        v -> v.attribute.getText(), belongTo.bp.calc
                );
                StatusRandomStatus.install(from, belongTo, StatusType.BUFF, choose, 2);
            };
            runOn(Trigger.BEFORE_ROUND, action);
            if (Skill2.this.awakening) {
                runOn(Trigger.OUT_ROUND_ACTION, action);
            }
        }

        private static class StatusXZShield extends StatusShield {
            public StatusXZShield(Character from, Character belongTo, double shield) {
                super(from, belongTo, shield);
                duration(StatusDurationType.CHI_XU, 2);
            }
        }
    }

    private static class StatusRandomStatus extends Status {
        private final RandomStatus randomStatus;

        private StatusRandomStatus(Character from, Character belongTo, StatusType statusType,
                                   RandomStatus randomStatus, int duration
        ) {
            super(SkillName + "DEBUFF", from, belongTo, statusType, StatusForm.ZHUANG_TAI);
            this.randomStatus = randomStatus;
            duration(StatusDurationType.CHI_XU, duration);
            attribute(randomStatus.attribute, _ -> {
                double rtValue = switch (randomStatus) {
                    case ATTACK -> belongTo.getInitAttack() * 0.1;
                    case DEFENSE -> belongTo.getInitDefense() * 0.2;
                    case CRIT_POWER, SPEED, EFFECT_RESIST_RATE -> 10;
                };
                if (statusType == StatusType.DEBUFF) {
                    rtValue = -rtValue;
                }
                return rtValue;
            });
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
                    status.duration(duration);
                    return;
                }
            }
            belongTo.addStatus(new StatusRandomStatus(from, belongTo, statusType, randomStatus, duration));

        }
    }

    private enum RandomStatus {
        ATTACK(Attribute.ATTACK),
        DEFENSE(Attribute.DEFENCE),
        CRIT_POWER(Attribute.CRIT_POWER),
        SPEED(Attribute.SPEED),
        EFFECT_RESIST_RATE(Attribute.EFFECT_RESIST_RATE);

        private final Attribute attribute;

        RandomStatus(Attribute attribute) {
            this.attribute = attribute;
        }
    }
}
