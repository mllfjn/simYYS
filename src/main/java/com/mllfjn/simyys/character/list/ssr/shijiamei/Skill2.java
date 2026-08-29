package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "爱见舍离";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);

        // 持有两把刃时获得的效果
        belongTo.addStatus(new StatusRenBuff(belongTo, level));
        // 敌方式神回合外行动监听
        belongTo.bp.addStatusAdder(c ->
                c.team != belongTo.team && c.isShiShen()
                        ? new StatusOutRoundActionListener(belongTo, c)
                        : null
        );
        // 先机获得花祓
        if (level >= 5) {
            belongTo.bp.addPriorityMove(belongTo, () -> StatusHuaFu.addStack(belongTo, 2));
        }
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t战斗开始时,获得60%效果抵抗;解锁断罪之刃时,额外获得50%暴击
                √\t敌方式神回合外行动时,消耗1层花祓,并将其困入邪执
                √\t[释放]获得2层花祓,并提升自身100点速度,持续2回合
                √\tlv2-持垂悯之刃,获得450点防御
                √\tlv3-释放可获得3层花祓
                √\tlv4-持断罪之刃,攻击无视350点防御
                √\tlv5-先机:获得2层花祓
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        StatusHuaFu.addStack(getBelongTo(), getLevel() >= 3 ? 3 : 2);
        StatusSpeed.install(getBelongTo());

        return Optional.empty();
    }

    static class StatusRenBuff extends Status implements AttributeModifier {

        // skill2-lv2默认获得防御
        private boolean isIncreaseDefense;
        // skill2-lv4断罪获得无视防御
        private boolean isIgnoreDefense;
        // 是否解锁断罪之刃
        private boolean isUnlock = false;

        public StatusRenBuff(Character character, int level) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);

            if (level >= 2) {
                isIncreaseDefense = true;
                if (level >= 4) {
                    isIgnoreDefense = true;
                }
            }
        }

        void unlock() {
            isUnlock = true;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.EFFECT_RESIST_RATE
                    || isIncreaseDefense && attribute == Attribute.DEFENCE
                    || isIgnoreDefense && attribute == Attribute.IGNORE_DEFENCE && isUnlock
                    || attribute == Attribute.CRIT_RATE && isUnlock;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            if (attribute == Attribute.EFFECT_RESIST_RATE) {
                return 60;
            } else if (attribute == Attribute.DEFENCE) {
                return 450;
            } else if (attribute == Attribute.IGNORE_DEFENCE) {
                return 350;
            } else {
                return 50;
            }
        }
    }

    static class StatusSpeed extends StatusModifyAttribute {
        private StatusSpeed(Character character) {
            super(character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        static void install(Character character) {
            character.getStatus(StatusSpeed.class)
                    .ifPresentOrElse(
                            status -> status.duration(2),
                            () -> character.addStatus(new StatusSpeed(character))
                    );
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 100;
        }
    }

    static class StatusOutRoundActionListener extends Status implements StatusRunnable {
        public StatusOutRoundActionListener(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.OUT_ROUND_ACTION && StatusHuaFu.consumeStack(from);
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            belongTo.addStatus(new StatusXieZhi(from, belongTo));
            return false;
        }
    }
}
