package com.mllfjn.simyys.character.list.yys.yuanlaiguang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.instance.StatusShield;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.InteractiveInfo;

class Skill6Passive extends PassiveSkill {
    static final String SkillName = "鬼胄";

    private final StatusShieldGZ status;
    private StatusGZFS statusGZFS;

    public Skill6Passive(Character belongTo, int shuYin) {
        super(belongTo, 0, 6);
        status = new StatusShieldGZ(belongTo, shuYin);
        belongTo.addStatus(status);
    }

    public void fuShen(Character target) {
        if (statusGZFS != null) {
            statusGZFS.belongTo.removeStatus(statusGZFS);
            statusGZFS = null;
        }
        statusGZFS = new StatusGZFS(getBelongTo(), target, status);
        target.addStatus(statusGZFS);
    }

    public void addAbsorb(double addAbsorb) {
        status.addAbsorb(addAbsorb);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusShieldGZ extends StatusShield implements StatusRunnable, Displayable {
        private static final String StatusName = "鬼兵部";
        private double maxShield;
        private double absorb;

        public StatusShieldGZ(Character character, int shuYin) {
            super(character, character, 0);
            absorb = 0.3 + shuYin * 0.05;
            reset();
        }

        public void addAbsorb(double addAbsorb) {
            this.absorb += addAbsorb;
        }

        private void reset() {
            // 至多吸收源赖光初始攻击600%伤害
            maxShield = belongTo.getInitAttack() * 6;
        }

        @Override
        public boolean handle(InteractiveInfo interactiveInfo) {
            if (maxShield > 0) {
                // 每次受到攻击时，为其吸收源赖光初始攻击30%的伤害
                double maxUse = Math.min(belongTo.getInitAttack() * absorb, maxShield);
                double number = interactiveInfo.getTraceableNumber().getNumber();

                double realUsed = Math.min(number, maxUse);

                interactiveInfo.getTraceableNumber().sub(realUsed, Skill6Passive.SkillName);
                maxShield -= realUsed;
            }
            return false;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND || trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.BEFORE_ROUND) {
                // 源赖光回合开始时刷新此上限
                reset();
            } else if (trigger == Trigger.AFTER_ATTACK) {
                belongTo.getSkill(7).ifPresent(skill -> {
                    AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                    // 鬼兵部附身的目标受到攻击但未受到伤害时，鬼兵部劈斩目标来源
                    if (attackInfo.getTraceableNumber().getNumber() == 0) {
                        ((Skill7Passive) skill).piZhan(attackInfo.getAttacker());
                    }
                });
            }

            return false;
        }

        @Override
        public String getDisplayText() {
            return StatusName;
        }
    }

    static class StatusGZFS extends StatusShield implements Displayable {
        private final StatusShieldGZ status;

        public StatusGZFS(Character from, Character belongTo, StatusShieldGZ status) {
            super(from, belongTo, 0);
            this.status = status;
        }

        @Override
        public String getDisplayText() {
            return status.getDisplayText();
        }

        @Override
        public boolean handle(InteractiveInfo interactiveInfo) {
            return status.handle(interactiveInfo);
        }
    }
}
