package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

public class WuDaoQu extends Equip implements YuHunAttack {
    public static final String EQUIP_NAME = "无刀取";

    private StatusRecord statusRecord;

    @Override
    public String getName() {
        return EQUIP_NAME;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        statusRecord = new StatusRecord(character);
    }

    @Override
    public void effectInfo(AttackInfo attackInfo) {
        if (statusRecord.getStack() > 0) {
            attackInfo.getTraceableNumber().mul(1 + 0.15 * statusRecord.getStack(), EQUIP_NAME);
            yuHunEffect();
        }
    }

    private static class StatusRecord extends Status {

        protected StatusRecord(Character character) {
            super("无刀取计数器", character);
            enableStack(3);
            displayNameAndNonZeroStack();
            runOn(Trigger.AFTER_ROUND, _ -> {
                stack++;
                if (stack == 3) {
                    disableAction(Trigger.AFTER_ROUND);
                }
            });
            retainAfterDie();
            retainAfterChangeWave(() -> {
                stack = 0;
                enableAction(Trigger.AFTER_ROUND);
            });
            addTo();
        }
    }
}
