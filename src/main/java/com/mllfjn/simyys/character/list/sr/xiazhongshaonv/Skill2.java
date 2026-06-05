package com.mllfjn.simyys.character.list.sr.xiazhongshaonv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.StatusSupplier;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "溢彩";
    private static final List<String> buffs = List.of(
            StatusIncreaseAttack.StatusName,
            StatusIncreaseDefense.StatusName,
            StatusIncreaseCritPower.StatusName,
            StatusIncreaseSpeed.StatusName,
            StatusIncreaseEffectResist.StatusName
    );

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
            String choose = RateController.choose("匣中少女-溢彩:为攻击者附加减益", buffs, s -> s, belongTo.bp.calc);
            StatusSupplier supplier = switch (choose) {
                case StatusIncreaseAttack.StatusName -> new StatusSupplier(StatusIncreaseAttack.StatusName,
                        StatusReduceAttack.class, (from, to) ->
                        to.replaceStatus(new StatusReduceAttack(belongTo, attacker))
                );
                case StatusIncreaseDefense.StatusName -> new StatusSupplier(StatusIncreaseDefense.StatusName,
                        StatusReduceDefense.class, (from, to) ->
                        to.replaceStatus(new StatusReduceDefense(belongTo, attacker))
                );
                case StatusIncreaseCritPower.StatusName -> new StatusSupplier(StatusIncreaseCritPower.StatusName,
                        StatusReduceCritPower.class, (from, to) ->
                        to.replaceStatus(new StatusReduceCritPower(belongTo, attacker))
                );
                case StatusIncreaseSpeed.StatusName -> new StatusSupplier(StatusIncreaseSpeed.StatusName,
                        StatusReduceSpeed.class, (from, to) ->
                        to.replaceStatus(new StatusReduceSpeed(belongTo, attacker))
                );
                case StatusIncreaseEffectResist.StatusName -> new StatusSupplier(StatusIncreaseEffectResist.StatusName,
                        StatusReduceEffectResist.class, (from, to) ->
                        to.replaceStatus(new StatusReduceEffectResist(belongTo, attacker))
                );
                default -> throw new IllegalStateException("Unexpected value: " + choose);
            };
            belongTo.doInteractive(interactive -> interactive.effect(Skill2.this, attacker,
                    100, 0, true, supplier)
            );
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
            String choose = RateController.choose("匣中少女-溢彩:获得BUFF", buffs, s -> s, belongTo.bp.calc);
            switch (choose) {
                case StatusIncreaseAttack.StatusName -> belongTo.replaceStatus(new StatusReduceAttack(from, belongTo));
                case StatusIncreaseDefense.StatusName ->
                        belongTo.replaceStatus(new StatusReduceDefense(from, belongTo));
                case StatusIncreaseCritPower.StatusName ->
                        belongTo.replaceStatus(new StatusReduceCritPower(from, belongTo));
                case StatusIncreaseSpeed.StatusName -> belongTo.replaceStatus(new StatusReduceSpeed(from, belongTo));
                case StatusIncreaseEffectResist.StatusName ->
                        belongTo.replaceStatus(new StatusReduceEffectResist(from, belongTo));
            }
            return false;
        }

        private static class StatusXZShield extends StatusShield {
            public StatusXZShield(Character from, Character belongTo, double shield) {
                super(from, belongTo, shield);
                setDurationType(StatusDurationType.CHI_XU, 2);
            }
        }
    }

    private static class StatusIncreaseAttack extends Status implements AttributeModifier {
        private static final String StatusName = "攻击";

        public StatusIncreaseAttack(Character from, Character belongTo) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return belongTo.getInitAttack() * 0.1;
        }
    }

    private static class StatusIncreaseDefense extends Status implements AttributeModifier {
        private static final String StatusName = "防御";

        public StatusIncreaseDefense(Character from, Character belongTo) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return belongTo.getInitDefense() * 0.2;
        }
    }

    private static class StatusIncreaseCritPower extends Status implements AttributeModifier {
        private static final String StatusName = "爆伤";

        public StatusIncreaseCritPower(Character from, Character belongTo) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.CRIT_POWER;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 10;
        }
    }

    private static class StatusIncreaseSpeed extends Status implements AttributeModifier {
        private static final String StatusName = "速度";

        public StatusIncreaseSpeed(Character from, Character belongTo) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 10;
        }
    }

    private static class StatusIncreaseEffectResist extends Status implements AttributeModifier {
        private static final String StatusName = "抵抗";

        public StatusIncreaseEffectResist(Character from, Character belongTo) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.EFFECT_RESIST_RATE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 10;
        }
    }

    private static class StatusReduceAttack extends Status implements AttributeModifier {
        public StatusReduceAttack(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return -belongTo.getInitAttack() * 0.1;
        }
    }

    private static class StatusReduceDefense extends Status implements AttributeModifier {
        public StatusReduceDefense(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return -belongTo.getInitDefense() * 0.2;
        }
    }

    private static class StatusReduceCritPower extends Status implements AttributeModifier {
        public StatusReduceCritPower(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.CRIT_POWER;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return -10;
        }
    }

    private static class StatusReduceSpeed extends Status implements AttributeModifier {
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
            return -10;
        }
    }

    private static class StatusReduceEffectResist extends Status implements AttributeModifier {
        public StatusReduceEffectResist(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.EFFECT_RESIST_RATE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return -10;
        }
    }
}
