package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.InteractiveInfo;

class StatusXieDu extends Status implements Displayable, AttributeModifier, StatusRunnable {
    public static final String StatusName = "蝎毒";
    public static final Skill SKILL = Skill.getInstance(StatusName);

    private final boolean setOffAfterRound;
    private final int multiplier;
    private int stack = 1;

    public StatusXieDu(com.mllfjn.simyys.character.Character from, Character belongTo, int multiplier, boolean setOffAfterRound) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        this.setOffAfterRound = setOffAfterRound;
        this.multiplier = multiplier;
    }

    public void setOff() {
        AttackInfo info = AttackInfo.createJianJieAttack(from, SKILL, belongTo,
                (owner, target) -> from.getAttack() * multiplier);
        from.doInteractive(interactive -> interactive.attack(info, AttackType.JIAN_JIE));
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
                || trigger == Trigger.AFTER_ATTACK;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.AFTER_ROUND_FIRST) {
            setOff();
        } else if (param instanceof ParamAfterAttack paa) {
//            paa.interactiveInfo.
        }
        return false;
    }
}
