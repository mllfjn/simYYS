package com.mllfjn.simyys.character.list.sp.sphudie;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "灵梦";

    private final boolean immuneOverDoseDamage;
    private final boolean reinforcement;

    private boolean used = false;
    private double damage;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 1, 0, 2);
        if (level >= 4) {
            Status status = Status.of(SkillName + "-受到攻击监听", belongTo);
            status.retainAfterDie()
                    .retainAfterChangeWave()
                    .runOn(Trigger.AFTER_ATTACK, param -> {
                        double number = ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().getNumber();
                        damage += number;
                        // TODO 这里有一个初始生命的概念,以后加上
                        if (damage >= (belongTo.getMaxHp() * 0.2)) {
                            belongTo.bp.addOutRoundSkill(this, () -> {
                                huDieSkill2Use();
                                damage = 0;
                                status.enableAction(Trigger.AFTER_ATTACK);
                            });
                        }
                    })
                    .runOnAndDisable(Trigger.BEFORE_ROUND, _ -> {
                        status.enableAction(Trigger.AFTER_ATTACK);
                        status.disableAction(Trigger.BEFORE_ROUND);
                    })
                    .addTo();
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
}
