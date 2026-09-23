package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

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

    static class StatusRenBuff extends Status {
        // skill2-lv4断罪获得无视防御
        private boolean isIgnoreDefense;

        public StatusRenBuff(Character character, int level) {
            super(SkillName + "BUFF", character);
            attribute(Attribute.EFFECT_RESIST_RATE, 60);

            // skill2-lv2默认获得防御
            if (level >= 2) {
                attribute(Attribute.DEFENCE, 450);
            }

            if (level >= 2) {
                if (level >= 4) {
                    isIgnoreDefense = true;
                }
            }
        }

        // 解锁断罪之刃
        void unlock() {
            attribute(Attribute.CRIT_RATE, 50);
            if (isIgnoreDefense) {
                attribute(Attribute.IGNORE_DEFENCE, 350);
            }
        }
    }

    static class StatusSpeed extends Status {
        private StatusSpeed(Character character) {
            super(SkillName + "加速", character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 2);
            attribute(Attribute.SPEED, 100);
        }

        static void install(Character character) {
            character.getStatus(StatusSpeed.class)
                    .ifPresentOrElse(
                            status -> status.duration(2),
                            () -> character.addStatus(new StatusSpeed(character))
                    );
        }
    }

    static class StatusOutRoundActionListener extends Status {
        public StatusOutRoundActionListener(Character from, Character belongTo) {
            super(SkillName + "回合外行动监听", from, belongTo);
            runOn(Trigger.OUT_ROUND_ACTION, _ -> {
                if (StatusHuaFu.consumeStack(from)) {
                    belongTo.addStatus(new StatusXieZhi(from, belongTo));
                }
            });
        }
    }
}
