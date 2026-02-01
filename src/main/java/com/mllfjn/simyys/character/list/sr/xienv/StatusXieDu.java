package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventRoundDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;

class StatusXieDu extends Status implements Displayable, AttributeModifier, StatusRunnable {
    public static final String StatusName = "蝎毒";
    public static final Skill SKILL = Skill.getInstance(StatusName);

    private final int multiplier;
    private final boolean setOffAfterRound;
    private final Skill2 skill2;

    private int stack = 1;
    private double receivedDamage = 0;

    public StatusXieDu(Character from, Character belongTo, int multiplier,
                       boolean setOffAfterRound, Skill2 skill2) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        this.multiplier = multiplier;
        this.setOffAfterRound = setOffAfterRound;
        this.skill2 = skill2;
    }

    public void setOff() {
        AttackInfo info = AttackInfo.createJianJieAttack(from, SKILL, belongTo,
                (owner, target) -> from.getAttack());
        info.setMultiplier(multiplier);
        from.doInteractive(interactive -> interactive.attack(info));
        if (stack < 5) {
            stack++;
        }
    }

    public int getStack() {
        return stack;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return -stack * 80;
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return (setOffAfterRound && trigger == Trigger.AFTER_ROUND_FIRST)
                || (stack == 5 && trigger == Trigger.AFTER_ATTACK && skill2.canCount());
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.AFTER_ROUND_FIRST) {
            setOff();
        } else if (param instanceof ParamAfterAttack paa && paa.attackInfo.getAttackType() == AttackType.JIAN_JIE) {
            if (receivedDamage == 0) {
                from.bp.addActionListener(from, event -> {
                    if (event instanceof EventRoundDone) {
                        jianShe();
                        return true;
                    }
                    return false;
                });
            }
            receivedDamage += paa.attackInfo.getTraceableNumber().getNumber();
        }
        return false;
    }

    private void jianShe() {
        List<Character> list = new CharacterFinder(from)
                .filterEnemy()
                .getList();
        list.remove(belongTo);
        double average = receivedDamage / list.size() * 0.6;
        from.doInteractive(interactive ->
                interactive.attack(SKILL, list, (target) ->
                        AttackInfo.createJianJieAttack(
                                from,
                                SKILL,
                                target,
                                (from, to) -> average
                        )
                )
        );
        receivedDamage = 0;
    }
}
