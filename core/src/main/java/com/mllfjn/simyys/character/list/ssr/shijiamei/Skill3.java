package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

class Skill3 extends Skill {
    private static final String SkillName = "寂灭现前";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, level >= 2 ? 2 : 3, 0, 3);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t获得2层幻花,为友方提升30%效果抵抗,持续2回合
                √\t并为生命最低的3个友方恢复初始攻击130%生命
                √\t持断罪之刃可额外攻击敌方指定和生命最低的2个目标,依次造成攻击339%伤害
                √\tlv2-未解锁断罪之刃时,鬼火消耗减少1点
                √\tlv3-对虚妄迷阵中的敌方伤害系数提升30%
                √\tlv4-释放可获得1层花祓
                √\tlv5-攻击虚妄迷阵中的敌方额外无视100防御
                """;
    }

    void unlock() {
        if (getLevel() >= 2) {
            setCost(3);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        ShiJiaMei belongTo = (ShiJiaMei) getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        int level = getLevel();

        // 友方目标
        List<Character> teammates = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();
        // 提供抵抗
        for (Character teammate : teammates) {
            StatusEffectResist.install(belongTo, teammate);
        }

        // 友方生命排序
        PriorityQueue<Character> priorityQueueRecovery = new CharacterFinder(belongTo)
                .filterTeammate()
                .getPriorityQueue(Attribute.HP, CharacterFinder.Criteria.MIN);

        double recovery = belongTo.getInitAttack() * 1.3;
        for (int i = 0; i < 3; i++) {
            Character next = priorityQueueRecovery.poll();
            if (next != null) {
                interactive.recovery(this, next, recovery);
            } else {
                break;
            }
        }

        if (level >= 4) {
            StatusHuaFu.addStack(belongTo, 1);
        }

        // 持断罪之刃攻击
        if (belongTo.isUnlockDuanZui()) {
            // 5级无视100点防御
            if (level >= 5) {
                belongTo.addStatus(new StatusIgnoreDefense(belongTo));
            }

            Character target = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MIN);
            int targetMultiplier = getMultiplier(target);
            interactive.attackTypical(this, target, targetMultiplier, AttackType.DAN_TI);

            // 逻辑有点乱,目的是为了减少调用getMultiplier方法
            PriorityQueue<Character> priorityQueueAttack = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .getPriorityQueue(Attribute.HP, CharacterFinder.Criteria.MIN);
            Character first = priorityQueueAttack.poll();
            // != null代表了还有目标需要攻击
            if (first != null) {
                int firstMultiplier = first == target ? targetMultiplier : getMultiplier(first);
                interactive.attackTypical(this, first, firstMultiplier, AttackType.DAN_TI);

                Character second = priorityQueueAttack.poll();
                // second == null 代表了只剩一个目标了,如果它还活着再攻击一次
                if (second == null) {
                    if (first.alive) {
                        interactive.attackTypical(this, first, firstMultiplier, AttackType.DAN_TI);
                    }
                } else {
                    int secondMultiplier = second == target ? targetMultiplier : getMultiplier(second);
                    interactive.attackTypical(this, second, secondMultiplier, AttackType.DAN_TI);
                }
            }
        }

        // 2层幻花,注意这个要放在造成伤害之后
        StatusHuanHua.addStack(belongTo, 2);

        return Optional.empty();
    }

    private int getMultiplier(Character character) {
        if (getLevel() >= 3) {
            return character.getStatus(StatusXuWangMiZhang.class).isPresent() ? 369 : 339;
        } else {
            return 339;
        }
    }

    static class StatusEffectResist extends Status {
        private StatusEffectResist(Character from, Character belongTo) {
            super(SkillName + "效果抵抗", from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 2);
            attribute(Attribute.EFFECT_RESIST_RATE, 30);
        }

        private static void install(Character from, Character belongTo) {
            belongTo.getStatus(StatusEffectResist.class)
                    .ifPresentOrElse(
                            status -> status.duration(2),
                            () -> belongTo.addStatus(new StatusEffectResist(from, belongTo))
                    );
        }
    }

    static class StatusIgnoreDefense extends Status {
        public StatusIgnoreDefense(Character character) {
            super(SkillName + "无视防御", character);
            attribute(Attribute.IGNORE_DEFENCE, param ->
                    (double) (param.target().isHaveStatus(StatusXuWangMiZhang.class) ? 100 : 0)
            );
            runOn(Trigger.AFTER_ACTION, _ -> delete());
        }
    }
}
