package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;

class QiLingTiHun {
    static final String QiLingName = "薙魂";
    private static final Skill SKILL = Skill.getInstance(QiLingName);

    public static void install(Character character) {
        character.addStatus(new StatusQLTHListener(character));
    }

    private static class StatusQLTHListener extends Status {
        private int cooling = 0;
        private int remainingTimes = 3;

        public StatusQLTHListener(Character character) {
            super(QiLingName + "监听", character);
            StatusAdder<?> adder = character.bp.addStatusAdder(c ->
                    c.team == character.team && c != character
                            ? new StatusTHPreventDie(character, c)
                            : null
            );
            beforeDelete(adder::deleteAndRemove);
            // 未在冷却时血量改变时触发
            runOn(Trigger.HP_CHANGE, _ -> check());

            // 使用后计算冷却
            runOnAndDisable(Trigger.AFTER_ROUND, _ -> {
                cooling--;
                if (cooling == 0) {
                    check();
                } else {
                    enableAction(Trigger.HP_CHANGE);
                    disableAction(Trigger.AFTER_ROUND);
                }
            });
        }

        public void takeEffect() {
            if (remainingTimes == 1) {
                delete();
            } else {
                remainingTimes--;
            }
        }

        private void check() {
            // 阴阳师生命小于等于50%时,薙魂攻击敌方全体,造成100%的伤害
            if (belongTo.getHpPercent() < 0.5) {
                QiLingFactory.yuHunEffect(belongTo, QiLingName);
                List<Character> enemies = new CharacterFinder(belongTo)
                        .filterEnemy()
                        .getList();
                belongTo.doInteractive(interactive ->
                        interactive.attackTypical(SKILL, enemies, 100, AttackType.QUN_TI)
                );
                // 并提升友方全体30%伤害,持续2回合
                List<Character> teammates = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .getList();
                for (Character teammate : teammates) {
                    StatusTHZengShang.install(belongTo, teammate);
                }
                // 冷却2回合,好像实际是3回合
                cooling = 3;
                disableAction(Trigger.HP_CHANGE);
                enableAction(Trigger.AFTER_ROUND);
            }
        }

        class StatusTHPreventDie extends Status {

            public StatusTHPreventDie(Character from, Character belongTo) {
                super(QiLingName + "免死", from, belongTo);
                preventDie(excessDamage -> {
                    StatusQLTHListener.this.takeEffect();
                    belongTo.setHp(belongTo.getMaxHp() * 0.2);
                    from.beHurt(AttackInfo.createRealAttack(from, SKILL, from, excessDamage));
                });
            }
        }

        static class StatusTHZengShang extends Status {

            private StatusTHZengShang(Character from, Character belongTo) {
                super("契薙", from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
                duration(StatusDurationType.CHI_XU, 2);
                attribute(Attribute.ZENG_SHANG, 30);
                displayNameAndDuration();
            }

            public static void install(Character from, Character belongTo) {
                belongTo.getStatus(StatusTHZengShang.class)
                        .ifPresentOrElse(
                                status -> status.duration(2),
                                () -> belongTo.addStatus(new StatusTHZengShang(from, belongTo))
                        );
            }
        }
    }
}
