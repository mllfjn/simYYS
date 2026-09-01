package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class StatusZSZH extends Status {
    private static final String StatusName = "智识之火";

    private final StatusQMCauseAttackListener status;

    public StatusZSZH(Character from, Character belongTo, int critPower, SkillQiMeng qm) {
        super(StatusName, from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        status = new StatusQMCauseAttackListener(qm, from, belongTo);
        qm.addCharacter(belongTo);
        beforeDelete(() -> qm.removeCharacter(belongTo));
        attribute(Attribute.CRIT_POWER, critPower);
        displayName();
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
