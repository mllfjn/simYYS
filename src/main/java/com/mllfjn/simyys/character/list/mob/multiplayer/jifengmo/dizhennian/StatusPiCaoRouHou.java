package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

class StatusPiCaoRouHou extends Status implements StatusRunnable, Displayable {
    private static final String StatusName = "皮糙肉厚";

    private final double breakDamage;

    private int stack = 10;

    public StatusPiCaoRouHou(Character character, double breakDamage) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.breakDamage = breakDamage;

        belongTo.bp.atBattleStart(() -> {
            for (Character target : belongTo.bp.situation.characters) {
                target.addStatus(new StatusPiCaoRouHouReduceDamage(character, target));
            }
        });
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ATTACK;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (param instanceof ParamAttackInfo paa) {
            double number = paa.attackInfo.getTraceableNumber().getNumber();
            if (number > breakDamage) {
                stack--;
                return stack == 0;
            }
        }
        return false;
    }

    @Override
    public void beforeDelete() {
        for (Character character : belongTo.bp.situation.characters) {
            character.removeStatus(StatusPiCaoRouHouReduceDamage.class);
        }
        Character enemyYYS = new CharacterFinder(belongTo)
                .filterEnemy()
                .getFirst();
        Skill skill = Skill.getInstance(StatusName);
        AttackInfo attackInfo = AttackInfo.createRealAttack(enemyYYS, skill, belongTo,
                (c1, c2) -> 5000000.0);
        enemyYYS.doInteractive(interactive -> interactive.attack(attackInfo));
    }

    static class StatusPiCaoRouHouReduceDamage extends Status implements InfluenceDamageBeingAttack {
        public StatusPiCaoRouHouReduceDamage(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void doInfluenceBeingAttack(AttackInfo attackInfo) {
            if (attackInfo.getAttackType() != AttackType.ZHEN_SHI) {
                attackInfo.getTraceableNumber().mul(0.3, StatusPiCaoRouHou.StatusName);
            }
        }
    }
}
