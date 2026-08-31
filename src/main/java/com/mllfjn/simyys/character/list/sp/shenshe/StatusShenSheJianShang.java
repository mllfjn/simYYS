package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;

public class StatusShenSheJianShang extends Status {
    int count;

    public StatusShenSheJianShang(Character character) {
        super("神蛇堕落之剑减伤", character);
        runOn(Trigger.BEING_ATTACKED, triggerParam -> {
            // 每存在1把(count),神堕八岐大蛇受到的伤害减少20%
            ((ParamAttackInfo) triggerParam).getAttackInfo().getTraceableNumber()
                    .mul(Math.max(0, 1 - count * 0.2), "神蛇堕落之剑减伤");
        });
    }

    void add() {
        count++;
    }

    void reduce() {
        count--;
        if (count == 0) {
            delete();
        }
    }
}
