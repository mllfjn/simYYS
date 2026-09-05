package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

public class StatusZhiKao extends Status {
    private int stack;

    public StatusZhiKao(Character from, Character belongTo) {
        super("炙烤", from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        runOn(Trigger.BEING_ATTACKED, param -> {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (CharacterFactory.FIRE_CHARACTER.contains(attackInfo.getAttacker().getClass())) {

            }
        })
        addTo();
    }
}
