package com.mllfjn.simyys.character.list.r.chounv;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

public class CaoRen extends CharacterSummonBase {
    private static final String CharacterName = "诅咒草人";

    private static final double[] hpPercent = new double[]{0, 0.1, 0.15, 0.2, 0.25, 0.3};

    private final Character bind;

    public CaoRen(Character chouNv, Character target, int level) {
        super(chouNv.bp, CharacterName, target.team);
        this.bind = target;
        this.isSummon = true;

        this.setInitSpeed(target.getSpeed());

        this.setInitDefense(target.getDefence() * 0.5);
        this.forceSetMaxHp(target.getHp() * hpPercent[level], true);

        final Skill skill = Skill.getInstance(CharacterName);

        Status status = Status.of(CharacterName, chouNv, this);
        status.duration(StatusDurationType.CHI_XU, 3)
                .runOn(Trigger.AFTER_ATTACK, triggerParam -> {
                    double number = ((ParamAttackInfo) triggerParam).getAttackInfo().getTraceableNumber().getNumber();
                    chouNv.doInteractive(interactive -> {
                        interactive.attack(AttackInfo.createChuanDaoAttack(chouNv, skill, bind, number));
                        skill.useDone();
                    });
                })
                .display(() -> "剩余回合" + status.getDuration())
                .beforeDelete(this::die)
                .addTo();
    }

    public Character getBind() {
        return bind;
    }

    @Override
    public boolean isUncontrollable() {
        return true;
    }
}
