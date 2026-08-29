package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusConfusion;
import com.mllfjn.simyys.character.status.triggerParam.ParamHealInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.interactive.StatusSupplier;

import java.util.List;
import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "毒雾冲击";

    private final StatusDWCount statusDWCount;

    public Skill2(Character belongTo) {
        super(belongTo, 0, 0, 4, 2);
        statusDWCount = new StatusDWCount(belongTo, this);
        belongTo.addStatus(statusDWCount);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();

        List<Character> targets = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        // 对全体敌方单位分别造成2段伤害
        interactive.attackTypical(this, targets, 100, AttackType.QUN_TI);
        interactive.attackTypical(this, targets, 50, AttackType.QUN_TI);

        // 随后喷射毒雾，对全体敌方造成高额伤害
        interactive.attackTypical(this, targets, 100, AttackType.QUN_TI);

        // 同时有100%概率附加3回合的毒伤、禁疗、抵抗降低、攻击降低和速度降低效果
        // 这里的五种效果是可以分别抵抗的
        interactive.effect(this, targets, 100, true, StatusDuShang.getSupplier());
        interactive.effect(this, targets, 100, true, StatusJinLiao.getSupplier());
        interactive.effect(this, targets, 100, true, StatusResist.getSupplier());
        interactive.effect(this, targets, 100, true, StatusAttack.getSupplier());
        interactive.effect(this, targets, 100, true, StatusSpeed.getSupplier());

        // 25%概率附加1回合的混乱效果
        interactive.effect(this, targets, 25, 0, true
                , StatusConfusion.getSupplier(1));

        statusDWCount.setOff();

        return Optional.empty();
    }

    static class StatusDuShang extends Status {
        private static final String StatusName = "毒伤";

        private static final Skill skill = Skill.getInstance(StatusName);

        public StatusDuShang(Character from, Character belongTo) {
            super(StatusName, from, belongTo);
            type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 3);
            displayNameAndDuration();
            runOn(Trigger.BEFORE_ROUND, _ ->
                    from.doInteractive(interactive -> {
                        interactive.attackTypical(skill, belongTo, 33, AttackType.DAN_TI);
                        skill.useDone();
                    }));
        }

        public static StatusSupplier getSupplier() {
            return new StatusSupplier(StatusName, StatusDuShang.class, (from, to) ->
                    to.getStatus(StatusDuShang.class).ifPresentOrElse(
                            status -> {
                                if (status.getDuration() < 3) {
                                    status.duration(3);
                                }
                            },
                            () -> to.addStatus(new StatusDuShang(from, to))
                    )
            );
        }
    }

    static class StatusJinLiao extends Status {
        private static final String StatusName = "禁疗";

        public StatusJinLiao(Character from, Character belongTo) {
            super(StatusName, from, belongTo);
            type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 3);
            displayNameAndDuration();
            runOn(Trigger.WHEN_HEAL, triggerParam ->
                    ((ParamHealInfo) triggerParam).healInfo.getTraceableNumber().set(1, StatusName)
            );
        }

        public static StatusSupplier getSupplier() {
            return new StatusSupplier(StatusName, StatusJinLiao.class, (from, to) ->
                    to.getStatus(StatusJinLiao.class).ifPresentOrElse(
                            status -> {
                                if (status.getDuration() < 3) {
                                    status.duration(3);
                                }
                            },
                            () -> to.addStatus(new StatusJinLiao(from, to))
                    )
            );
        }
    }

    static class StatusResist extends Status {
        private static final String StatusName = "减抗";

        public StatusResist(Character from, Character belongTo) {
            super(StatusName, from, belongTo);
            type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 3);
            displayNameAndDuration();
            // 不知道降多少随便填的
            attribute(Attribute.EFFECT_RESIST_RATE, _ -> -20.0);
        }

        public static StatusSupplier getSupplier() {
            return new StatusSupplier(StatusName, StatusResist.class, (from, to) ->
                    to.getStatus(StatusResist.class).ifPresentOrElse(
                            status -> {
                                if (status.getDuration() < 3) {
                                    status.duration(3);
                                }
                            },
                            () -> to.addStatus(new StatusResist(from, to))
                    )
            );
        }
    }

    static class StatusAttack extends Status {
        private static final String StatusName = "减攻";

        public StatusAttack(Character from, Character belongTo) {
            super(StatusName, from, belongTo);
            type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 3);
            // 不知道降多少随便填的
            attribute(Attribute.ATTACK, _ -> -0.2 * belongTo.getInitAttack());
            displayNameAndDuration();
        }

        public static StatusSupplier getSupplier() {
            return new StatusSupplier(StatusName, StatusAttack.class, (from, to) ->
                    to.getStatus(StatusAttack.class).ifPresentOrElse(
                            status -> {
                                if (status.getDuration() < 3) {
                                    status.duration(3);
                                }
                            },
                            () -> to.addStatus(new StatusAttack(from, to))
                    )
            );
        }
    }

    static class StatusSpeed extends Status {
        private static final String StatusName = "减速";

        public StatusSpeed(Character from, Character belongTo) {
            super(StatusName, from, belongTo);
            type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 3);
            displayNameAndDuration();
            // 不知道降多少随便填的
            attribute(Attribute.SPEED, _ -> -30.0);
        }

        public static StatusSupplier getSupplier() {
            return new StatusSupplier(StatusName, StatusSpeed.class, (from, to) ->
                    to.getStatus(StatusSpeed.class).ifPresentOrElse(
                            status -> {
                                if (status.getDuration() < 3) {
                                    status.duration(3);
                                }
                            },
                            () -> to.addStatus(new StatusSpeed(from, to))
                    )
            );
        }
    }
}
