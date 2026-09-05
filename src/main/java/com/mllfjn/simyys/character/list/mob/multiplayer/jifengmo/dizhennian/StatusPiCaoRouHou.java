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

    private int stack = 10;

    public StatusPiCaoRouHou(Character character, double breakDamage) {
        super(StatusName, character);
        display(() -> StatusName + stack);
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
                    stack--;
                    if (stack == 0) {
                        delete();
                    }
                }
            }
        });

        belongTo.bp.addPriorityMove(belongTo, () -> {
            for (Character target : belongTo.bp.situation.characters) {
                target.addStatus(new StatusPiCaoRouHouReduceDamage(character, target));
            }
        });
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
