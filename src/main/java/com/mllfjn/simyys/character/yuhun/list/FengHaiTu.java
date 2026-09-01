package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.interactive.AttackInfo;

public class FengHaiTu extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "奉海图";

    private StatusAdder<?> adder;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        adder = character.bp.addStatusAdder(c ->
                c.team == character.team
                        ? new StatusHTListener(character, c)
                        : null
        );
    }

    @Override
    public void disable() {
        if (adder != null) {
            adder.deleteAndRemove();
            adder = null;
        }
    }

    class StatusHTListener extends Status {

        public StatusHTListener(Character from, Character belongTo) {
            super(YuHunName + "受到伤害监听", from, belongTo);
            runOn(Trigger.BEFORE_ATTACK, param ->
                    belongTo.getStatus(StatusHaiTuShouHu.class)
                            .orElseGet(
                                    () -> {
                                        StatusHaiTuShouHu status = new StatusHaiTuShouHu(from, belongTo);
                                        belongTo.addStatus(status);
                                        return status;
                                    }
                            ).reduce(((ParamAttackInfo) param).getAttackInfo())
            );
        }
    }

    class StatusHaiTuShouHu extends Status {
        private static final String StatusName = "海图守护";

        private double record;

        public StatusHaiTuShouHu(Character from, Character belongTo) {
            super(StatusName, from, belongTo);
            duration(StatusDurationType.CHI_XU, 1);
            beforeDelete(() -> belongTo.lostHP(record));
            display(() -> StatusName + ((int) record));
        }

        private void reduce(AttackInfo attackInfo) {
            double maxReduce = belongTo.getMaxHp() * 0.35;
            double expectedReduce = attackInfo.getTraceableNumber().getNumber() * 0.3;

            if (expectedReduce <= maxReduce) {
                attackInfo.getTraceableNumber().mul(0.7, StatusName);
                record += expectedReduce;
            } else {
                attackInfo.getTraceableNumber().sub(maxReduce, StatusName);
                record += maxReduce;
            }

            FengHaiTu.this.yuHunEffect();
        }
    }
}
