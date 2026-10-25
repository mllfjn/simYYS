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
        if (statusRecord.increase > 1) {
            attackInfo.getTraceableNumber().mul(statusRecord.increase, EQUIP_NAME);
            yuHunEffect();
        }
    }

    private static class StatusRecord extends Status {
        private double increase = 1;

        protected StatusRecord(Character character) {
            super("无刀取计数器", character);
            runOn(Trigger.AFTER_ROUND, _ -> {
                increase += 0.15;
                if (increase == 1.45) {
                    disableAction(Trigger.AFTER_ROUND);
                }
            });
            retainAfterDie();
            retainAfterChangeWave(() -> {
                increase = 1;
                enableAction(Trigger.AFTER_ROUND);
            });
            addTo();
        }
    }
}
