package com.mllfjn.simyys.character.list.sp.sphudie;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.RetainAfterChangeWave;
import com.mllfjn.simyys.character.status.determinant.RetainAfterDie;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "灵梦";

    private final boolean immuneOverDoseDamage;
    private final boolean reinforcement;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 1, 0, 2);
        if (level >= 4) {
            belongTo.addStatus(new StatusAFTERATTACKListener(belongTo));
        }
        immuneOverDoseDamage = level >= 3;
        reinforcement = level >= 5;
    }

    boolean isImmuneOverDoseDamage() {
        return immuneOverDoseDamage;
    }

    boolean isReinforcement() {
        return reinforcement;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        huDieSkill2Use();
        return Optional.empty();
    }

    void huDieSkill2Use() {
        Character belongTo = getBelongTo();
        int level = getLevel();
        StatusMengJian.install(belongTo, belongTo, (level >= 2 ? 0.2 : 0.15) * belongTo.getMaxHp(), this);
    }

    private class StatusAFTERATTACKListener extends Status implements StatusRunnable, RetainAfterDie, RetainAfterChangeWave {

        private boolean used = false;
        private double damage;

        public StatusAFTERATTACKListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return (!used && trigger == Trigger.AFTER_ATTACK) || (used && trigger == Trigger.BEFORE_ROUND);
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.BEFORE_ROUND) {
                used = false;
            } else {
                double number = ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().getNumber();
                damage += number;
                // TODO 这里有一个初始生命的概念,以后加上
                if (damage >= (belongTo.getMaxHp() * 0.2)) {
                    belongTo.bp.addOutRoundSkill(Skill2.this, () -> {
                        Skill2.this.huDieSkill2Use();
                        damage = 0;
                        used = true;
                    });
                }
            }
            return false;
        }
    }
}
