package com.mllfjn.simyys.character.list.sr.xuenv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.InteractiveInfo;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "冰甲术";

    private final StatusBJSListener status;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        status = new StatusBJSListener(belongTo, level);
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(status);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(status);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    class StatusBJSListener extends Status implements StatusRunnable {
        private static final int[] ShieldPercent = new int[]{0, 6, 9, 9, 12, 12};

        private final boolean reduceSpeed;
        private final int reduceSpeedRate;
        private final int shieldPercent;

        public StatusBJSListener(Character character, int level) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            reduceSpeed = level >= 3;
            reduceSpeedRate = level >= 5 ? 50 : 25;
            shieldPercent = ShieldPercent[level];
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            belongTo.addStatus(new StatusBJShield(belongTo, belongTo));
            Character character = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .filterSelf()
                    .get(Attribute.CRIT_POWER, CharacterFinder.Criteria.MAX);

            if (character != null) {
                character.addStatus(new StatusBJShield(belongTo, character));
            }

            return false;
        }

        class StatusBJShield extends StatusShield {

            public StatusBJShield(Character from, Character belongTo) {
                super(from, belongTo, from.getMaxHp() * shieldPercent / 100);
            }

            @Override
            public boolean handle(InteractiveInfo interactiveInfo) {
                if (reduceSpeed) {
                    from.doInteractive(interactive ->
                            interactive.effect(Skill2.this, interactiveInfo.getAttacker(),
                                    reduceSpeedRate, 0, true, StatusReduceSpeed.getSupplier()
                            )
                    );
                }
                return super.handle(interactiveInfo);
            }
        }
    }
}
