package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.StatusSupplier;

public class StatusChenLun extends Status implements CrowdControl, StatusRunnable, Displayable {
    private static final String text = "沉沦";
    private final int level;

    private StatusChenLun(Character from, Character belongTo, int level) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        this.level = level;
        setDurationType(StatusDurationType.CHI_XU, 1);
    }

    public static StatusSupplier getSupplier(int level) {
        return new StatusSupplier(text, StatusChenLun.class, (from, to) -> {
            if (!to.isHaveStatus(StatusDiaoLing.class)) {
                to.addStatus(new StatusChenLun(from, to, level));
            }
        });
    }

    @Override
    public String getDisplayText() {
        return text;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEING_ATTACKED;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
        // 受到来自伊邪那美与处于毁灭的目标在其自身回合内的攻击时,攻击者伤害提升
        Character attacker = attackInfo.getAttacker();
        if ((attacker == from || attacker.isHaveStatus(StatusHuiMie.class)) && attacker.isInRound()) {
            // 提升5%,lv5-提升增至10%
            attackInfo.getTraceableNumber().mul(level >= 5 ? 1.1 : 1.05, text);
        }

        return false;
    }
}
