package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class Skill4 extends Skill {
    private static final String SkillName = "凝视";

    private final Skill2 skillGuangQiu;

    public Skill4(Character belongTo, Skill2 skillGuangQiu) {
        super(belongTo, 0, 0, 0, 4);
        this.skillGuangQiu = skillGuangQiu;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        DiZhenNian diZhenNian = (DiZhenNian) getBelongTo();
        return super.canUse(bp)
                && diZhenNian.canNingShi()
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

    class StatusNingShiRecordDamage extends Status implements StatusRunnable, Displayable {
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
            } else if (trigger == Trigger.AFTER_ATTACK) {
                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                Character attacker = attackInfo.getAttacker();
                map.put(attacker
                        , map.getOrDefault(attacker, 0.0) + attackInfo.getTraceableNumber().getNumber());
            }
            return false;
        }

        @Override
        public void beforeDelete() {
            tuGuangQiu();
        }

        public void tuGuangQiu() {
            Optional<Character> oC = map.entrySet().stream()
                    .filter(entry -> entry.getKey() != ((DiZhenNian) belongTo).getHongNing())
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey);

            oC.ifPresent(c -> Skill4.this.skillGuangQiu.tuGuangQiu(Skill4.this, c));
        }

        @Override
        public String getDisplayText() {
            return SkillName + selfDuration;
        }

        static class StatusHongNing extends Status implements Displayable {

            public StatusHongNing(Character from, Character belongTo) {
                super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
                ((DiZhenNian) from).setHongNing(belongTo);
            }

            @Override
            public String getDisplayText() {
                return "红凝";
            }
        }

        static class StatusDZNBuffsDebuff extends Status implements AttributeModifier, Displayable {

            public StatusDZNBuffsDebuff(Character from, Character belongTo) {
                super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
                setDurationType(StatusDurationType.CHI_XU, 7);
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return attribute == Attribute.DEFENCE;
            }

            @Override
            public double getInfluence(Attribute attribute) {
                return -belongTo.getInitDefense();
            }

            @Override
            public String getDisplayText() {
                return "减防" + getDuration();
            }
        }
    }
}
