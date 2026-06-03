package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class StatusZSZH extends Status implements Displayable, AttributeModifier, StatusRunnable {
    private static final String StatusName = "智识之火";

    private final int critPower;
    private final SkillQiMeng qm;
    private final StatusQMCauseAttackListener status;

    public StatusZSZH(Character from, Character belongTo, int critPower, SkillQiMeng qm) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        this.qm = qm;
        this.critPower = critPower;
        status = new StatusQMCauseAttackListener(qm, from, belongTo);
        qm.addCharacter(belongTo);
    }

    @Override
    public void beforeDelete() {
        qm.removeCharacter(belongTo);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.CRIT_POWER;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return critPower;
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return status.runnable(trigger);
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        return status.run(trigger, bp, param);
    }
}
