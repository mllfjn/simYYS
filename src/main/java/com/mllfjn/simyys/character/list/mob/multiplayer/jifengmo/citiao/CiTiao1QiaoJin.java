package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.utils.serializable.SerialConsumer;

import java.util.List;
import java.util.Optional;

public class CiTiao1QiaoJin {
    public static final String CiTiaoName = "巧劲";

    public static void install(Character character) {
        character.bp().addStatusAdder(c -> c.team != character.team
                ? new StatusQJSplash(character, c)
                : null
        );

        // 己方（指玩家）攻击力最高的单位普攻时，额外获得一次行动，回合结束后增加自身35%行动条
        character.bp().addPriorityMove(character, () -> {
            Character maxAttack = new CharacterFinder(character)
                    .filterEnemy()
                    .get(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
            Status statusQJMax = Status.of("巧劲-攻击最高", character, maxAttack);
            statusQJMax.runOn(Trigger.USED_PU_GONG, _ -> {
                        Optional<StatusQJNewRoundMark> optional
                                = statusQJMax.belongTo.getStatus(StatusQJNewRoundMark.class);
                        if (optional.isEmpty()) {
                            statusQJMax.belongTo.addStatus(new StatusQJNewRoundMark(statusQJMax.from, statusQJMax.belongTo));
                            statusQJMax.belongTo.doInteractive(interactive -> {
                                interactive.getNewRound(statusQJMax.belongTo);
                                interactive.increaseLocation(statusQJMax.belongTo, 35);
                            });
                        }
                    })
                    .addTo();
        });
    }

    private static class StatusQJSplash extends Status {
        private final Skill skill = Skill.getInstance(CiTiao1QiaoJin.CiTiaoName);

        public StatusQJSplash(Character from, Character belongTo) {
            super("巧劲-伤害溅射", from, belongTo);
            SerialConsumer<TriggerParam> actionSplash = param -> {
                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                double number = attackInfo.getTraceableNumber().getNumber();
                List<Character> targets = new CharacterFinder(belongTo)
                        .filterEnemy()
                        .getList();
                targets.remove(attackInfo.getTarget());
                belongTo.doInteractive(interactive -> {
                    for (Character target : targets) {
                        interactive.attack(
                                AttackInfo.createRealAttack(belongTo, skill, target, number * 0.4)
                        );
                    }
                });
                skill.useDone();
            };
            SerialConsumer<TriggerParam> actionAfterRound = _ -> {
                StatusQJJianShang.addStack(from, belongTo);
                removeAction(Trigger.AFTER_ROUND);
            };
            runOn(Trigger.WILL_USE_PU_GONG, _ -> {
                runOn(Trigger.CAUSE_ATTACK, actionSplash);
                runOn(Trigger.USED_PU_GONG, _ -> {
                    removeAction(Trigger.CAUSE_ATTACK);
                    removeAction(Trigger.USED_PU_GONG);
                    runOn(Trigger.AFTER_ROUND, actionAfterRound);
                });
            });
        }
    }

    private static class StatusQJNewRoundMark extends Status {
        public StatusQJNewRoundMark(Character from, Character belongTo) {
            super("标记-巧劲不可连续触发", from, belongTo);
        }
    }

    static class StatusQJJianShang extends Status {
        private int stack = 1;

        private StatusQJJianShang(Character from, Character belongTo) {
            super("巧劲-减伤", from, belongTo);
            display(() -> CiTiao1QiaoJin.CiTiaoName + stack);
            attribute(Attribute.JIAN_SHANG, _ -> 5.0 * stack);
        }

        public static void addStack(Character from, Character target) {
            List<Character> list = new CharacterFinder(target)
                    .filterTeammate()
                    .getList();
            for (Character c : list) {
                c.addStatusOrChange(
                        StatusQJJianShang.class,
                        status -> status.stack = Math.min(30, status.stack + 1),
                        () -> new StatusQJJianShang(from, c)
                );
            }
        }
    }
}
