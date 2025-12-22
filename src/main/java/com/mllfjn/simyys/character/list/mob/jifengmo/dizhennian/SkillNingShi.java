package com.mllfjn.simyys.character.list.mob.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.InteractiveInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class SkillNingShi extends Skill {
    public static final String SkillName = "凝视";
    public static final int skillID = 5;

    public SkillNingShi(Character belongTo) {
        super(belongTo, 0, 0, 0, 5);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        DiZhenNian diZhenNian = (DiZhenNian) getBelongTo();
        return super.canUse(bp)
                && diZhenNian.getBuffType() != null
                && !diZhenNian.isHaveStatus(StatusNingShiRecordDamage.class);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        getBelongTo().addStatus(new StatusNingShiRecordDamage(getBelongTo()));

        return Optional.empty();
    }

    static class StatusNingShiRecordDamage extends Status implements StatusRunnable, Displayable {
        private final Map<Character, Double> map = new HashMap<>();

        private int selfDuration;

        public StatusNingShiRecordDamage(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            if (character.isInRound()) {
                selfDuration = 3;
            } else {
                selfDuration = 2;
            }
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK || trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.AFTER_ROUND) {
                selfDuration--;
                return selfDuration == 0;
            } else if (trigger == Trigger.AFTER_ATTACK && param instanceof ParamAfterAttack paa) {
                InteractiveInfo info = paa.interactiveInfo;
                Character attacker = info.getAttacker();
                map.put(attacker
                        , map.getOrDefault(attacker, 0.0) + info.getTraceableNumber().getNumber());
            }
            return false;
        }

        @Override
        public void beforeDelete() {
            tuGuangQiu();
        }

        public void tuGuangQiu() {
            DiZhenNian diZhenNian = (DiZhenNian) belongTo;
            Optional<Character> oC = map.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey);

            if (diZhenNian.isBeforeHouZi()) {
                diZhenNian.getSkill(SkillNingShi.skillID).ifPresent(skill -> skill.setCooling(2));
            }

            oC.ifPresent(character ->
                    character.addStatus(new StatusBuff(diZhenNian, character, diZhenNian.getBuffType(), 7)));
        }

        @Override
        public String getText() {
            return SkillName + selfDuration;
        }
    }
}
