package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;

class QiLingTiHun {
    static final String QiLingName = "薙魂";
    private static final Skill SKILL = Skill.getInstance(QiLingName);

    public static void install(Character character) {
        character.addStatus(new StatusQLTHListener(character));
    }

    static class StatusQLTHListener extends Status implements StatusRunnable {
        private final StatusAdder<?> adder;
        private int cooling = 0;
        private int remainingTimes = 3;

        public StatusQLTHListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            adder = character.bp.addStatusAdder(c ->
                    c.team == character.team && c != character
                            ? new StatusTHPreventDie(character, c)
                            : null
            );
        }

        public void takeEffect() {
            if (remainingTimes == 1) {
                delete();
            } else {
                remainingTimes--;
            }
        }

        @Override
        public void beforeDelete() {
            adder.deleteAndRemove();
        }

        @Override
        public boolean runnable(Trigger trigger) {
            if (cooling == 0) {
                // 未在冷却时血量改变时触发
                return trigger == Trigger.HP_CHANGE;
            } else {
                // 冷却时回合后触发
                return trigger == Trigger.AFTER_ROUND;
            }
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.HP_CHANGE) {
                if (belongTo.getHp() <= (belongTo.getMaxHp() / 2)) {
                    // 阴阳师生命小于等于50%时,薙魂攻击敌方全体,造成100%的伤害
                    action();
                }
            } else {
                cooling--;
                if (cooling == 0) {
                    action();
                }
            }
            return false;
        }

        private void action() {
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
        }

        class StatusTHPreventDie extends Status implements PreventDie {

            public StatusTHPreventDie(Character from, Character belongTo) {
                super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            }

            @Override
            public void preventDie(double excessDamage) {
                StatusQLTHListener.this.takeEffect();
                belongTo.setHp(belongTo.getMaxHp() * 0.2);
                from.beHurt(AttackInfo.createRealAttack(from, SKILL, from, excessDamage));
            }

            @Override
            public String getName() {
                return QiLingName;
            }
        }

        static class StatusTHZengShang extends Status implements AttributeModifier, Displayable {

            private StatusTHZengShang(Character from, Character belongTo) {
                super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
                setDurationType(StatusDurationType.CHI_XU, 2);
            }

            public static void install(Character from, Character belongTo) {
                belongTo.getStatus(StatusTHZengShang.class)
                        .ifPresentOrElse(
                                status -> status.setDuration(2),
                                () -> belongTo.addStatus(new StatusTHZengShang(from, belongTo))
                        );
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return attribute == Attribute.ZENG_SHANG;
            }

            @Override
            public double getInfluence(Attribute attribute, StatusModifyParam param) {
                return 30;
            }

            @Override
            public String getDisplayText() {
                return "契薙" + getDuration();
            }
        }
    }
}
