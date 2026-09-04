package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.yuhun.Equip;

public class HaiYueHuoYu extends Equip {
    public static final String YuHunName = "海月火玉";
    public static final int EXTRA_USE = 1;

    public void enable() {
        character.addStatus(new StatusHaiYue(character));
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    static class StatusHaiYue extends Status {

        public StatusHaiYue(Character character) {
            super("海月", character);
            duration(StatusDurationType.CHI_XU, 1);
            runOn(Trigger.WHEN_ATTACK, triggerParam ->
                    ((ParamAttackInfo) triggerParam).getAttackInfo().getTraceableNumber().mul(1.4, YuHunName)
            );
        }
    }
}
