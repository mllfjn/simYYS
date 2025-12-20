package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.ratecontroller.RateController;


// 该状态配合bp.forEveryone使用，给每个可以触发协战的目标上这个状态
// belongTo是刚发出普攻的单位 from是可以触发了协战会发出普攻的单位
public abstract class StatusXieZhan extends Status implements StatusRunnable {

    public StatusXieZhan(Character from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    protected abstract double getRate();

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.USE_PU_GONG;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (param instanceof ParamUseSkill pus) {
            Skill skill = pus.getSkill();
            Character target = pus.getTarget().orElseThrow();
            if (RateController.xieZhan(skill, from, target, bp.calc, getRate())) {
                from.xieZhan(skill, target);
            }
        }
        return false;
    }
}
