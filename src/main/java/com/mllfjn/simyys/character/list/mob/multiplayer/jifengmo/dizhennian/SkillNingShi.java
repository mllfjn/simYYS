package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.InteractiveInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class SkillNingShi extends Skill {
    public static final String SkillName = "凝视";
    public static final int skillID = 5;

    private final Skill skillGuangQiu;

    public SkillNingShi(Character belongTo) {
        super(belongTo, 0, 0, 0, 5);
        skillGuangQiu = new Skill(belongTo, 0, 0, 0, 0) {
            @Override
            public String getName() {
                return "光球";
            }

            @Override
            public Optional<Character> usePrivate(BattlePane bp) {
                return Optional.empty();
            }
        };
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
            } else if (trigger == Trigger.AFTER_ATTACK && param instanceof ParamAfterAttack paa) {
                InteractiveInfo info = paa.attackInfo;
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
                    .filter(entry -> entry.getKey() != diZhenNian.getHongNing())
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey);

            if (diZhenNian.isBeforeHouZi()) {
                diZhenNian.getSkill(SkillNingShi.skillID).ifPresent(skill -> skill.setCooling(2));
            }

            oC.ifPresent(character -> {
                character.getStatus(StatusBuff.class).ifPresent(statusExist -> {
                    // 已经有了BUFF
                    int duration = statusExist.getDuration();
                    // 剩余buff回合数*30%最大生命的穿盾伤害
                    AttackInfo info = AttackInfo
                            .createRealAttack(character, SkillNingShi.this.skillGuangQiu, character
                                    , (c1, c2) -> duration * 0.3 * character.getMaxHp());
                    info.setCanThroughShield(true);
                    character.doInteractive(interactive -> interactive.attack(info));
                    // 红凝
                    character.addStatus(new StatusHongNing(belongTo, character));
                });
                skillGuangQiu.log(character);
                character.addStatus(new StatusBuff(belongTo, character, diZhenNian.getBuffType(), 7));
            });
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
    }
}
