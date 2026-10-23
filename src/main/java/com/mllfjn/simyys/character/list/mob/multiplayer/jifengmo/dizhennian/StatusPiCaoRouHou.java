package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

class StatusPiCaoRouHou extends Status {
    private static final String StatusName = "皮糙肉厚";

    public StatusPiCaoRouHou(Character character, double breakDamage) {
        super(StatusName, character);
        enableStack(10);
        displayNameAndStack();
        beforeDelete(() -> {
            for (Character c : belongTo.bp.situation.characters) {
                c.removeStatus(StatusPiCaoRouHouReduceDamage.class);
            }
            Character enemyYYS = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .filterYYS(true)
                    .getFirst();
            Skill skill = Skill.getInstance(StatusName);
            AttackInfo attackInfo = AttackInfo.createRealAttack(enemyYYS, skill, belongTo, 5000000);
            enemyYYS.doInteractive(interactive -> interactive.attack(attackInfo));
        });
        runOn(Trigger.AFTER_ATTACK, triggerParam -> {
            AttackInfo attackInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
            if (attackInfo.getAttackType() == AttackType.DAN_TI) {
                double number = attackInfo.getTraceableNumber().getNumber();
                if (number > breakDamage) {
                    reduceStack();
                }
            }
        });

        belongTo.bp.addPriorityMove(belongTo, () -> {
            for (Character target : belongTo.bp.situation.characters) {
                target.addStatus(new StatusPiCaoRouHouReduceDamage(character, target));
            }
        });
    }

    @Override
    public void reduceStack() {
        super.reduceStack();
        if (stack == 0) {
            delete();
        }
    }

    static class StatusPiCaoRouHouReduceDamage extends Status {
        public StatusPiCaoRouHouReduceDamage(Character from, Character belongTo) {
            super("皮糙肉厚削减伤害", from, belongTo);
            runOn(Trigger.BEING_ATTACKED, triggerParam -> {
                AttackInfo attackInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
                if (attackInfo.getAttackType() != AttackType.CHUAN_DAO) {
                    attackInfo.getTraceableNumber().mul(0.3, StatusPiCaoRouHou.StatusName);
                }
            });
        }
    }
}
