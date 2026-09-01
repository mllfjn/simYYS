package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class HuoZhiChe extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "火之车";

    private StatusAfterRound statusAfterRound;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        statusAfterRound = new StatusAfterRound(character);
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        character.addStatus(statusAfterRound);
    }

    @Override
    public void disable() {
        character.removeStatus(statusAfterRound);
    }

    static class StatusAfterRound extends Status {
        private int stack;

        public StatusAfterRound(Character character) {
            super(YuHunName, character);
            display(() -> {
                if (stack == 0) {
                    return null;
                }
                return "墓火" + stack;
            });
            runOn(Trigger.AFTER_ROUND, _ -> {
                // 携带者回合结束时,获得1层墓火
                stack++;
                // 累计4层时清空层数并获得1个回合,且该回合鬼火消耗减少1点
                if (stack == 4) {
                    stack = 0;
                    belongTo.getInteractive().getNewRound(belongTo);
                    Status.of(YuHunName + "鬼火消耗减少", belongTo)
                            .duration(StatusDurationType.CHI_XU, 1)
                            .forceChangeSkillCost(-1)
                            .addTo();
                }
            });
        }
    }
}
