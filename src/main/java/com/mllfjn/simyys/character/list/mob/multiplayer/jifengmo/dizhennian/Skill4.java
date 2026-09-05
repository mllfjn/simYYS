package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class Skill4 extends Skill {
    private static final String SkillName = "鲶之凝视";

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

    class StatusNingShiRecordDamage extends Status {
        private final Map<Character, Double> map = new HashMap<>();

        public StatusNingShiRecordDamage(Character character) {
            super(SkillName, character);
            duration(StatusDurationType.CHI_XU, 2);
            beforeDelete(this::tuGuangQiu);
            displayNameAndDuration();
            runOn(Trigger.AFTER_ATTACK, triggerParam -> {
                AttackInfo attackInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
                Character attacker = attackInfo.getAttacker();
                map.put(attacker,
                        map.getOrDefault(attacker, 0.0) + attackInfo.getTraceableNumber().getNumber());

            });
        }

        public void tuGuangQiu() {
            Optional<Character> oC = map.entrySet().stream()
                    .filter(entry -> entry.getKey() != ((DiZhenNian) belongTo).getHongNing())
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey);

            oC.ifPresent(c -> Skill4.this.skillGuangQiu.tuGuangQiu(Skill4.this, c));
        }
    }
}
