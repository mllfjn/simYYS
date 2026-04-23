package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

public class StatusShenSheJianShang extends Status implements StatusRunnable {
    private int count;

    public StatusShenSheJianShang(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    public static void add(Character character) {
        character.getStatus(StatusShenSheJianShang.class)
                .or(() -> character.addStatus(new StatusShenSheJianShang(character)))
                .ifPresent(StatusShenSheJianShang::add);
    }

    public static void reduce(Character character) {
        character.getStatus(StatusShenSheJianShang.class).ifPresent(StatusShenSheJianShang::reduce);
    }

    private void add() {
        count++;
    }

    private void reduce() {
        count--;
        if (count == 0) {
            delete();
        }
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEING_ATTACKED;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        // 每存在1把(count),神堕八岐大蛇受到的伤害减少20%
        ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber()
                .mul(Math.max(0, 1 - count * 0.2), "神蛇堕落之剑减伤");
        return false;
    }
}
