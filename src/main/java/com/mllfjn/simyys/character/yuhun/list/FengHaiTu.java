package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;

public class FengHaiTu extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "奉海图";

    private BattleActionListener listener;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        listener = character.bp.forEveryone(character, c -> {
            if (c.team == character.team) {
                c.addStatus(new StatusHTListener(character, c));
            }
        });
    }

    @Override
    public void disable() {
        if (listener != null) {
            character.bp.removeActionListener(character, listener);
            List<Character> list = new CharacterFinder(character, true)
                    .filterTeammate()
                    .getList();
            for (Character c : list) {
                c.removeStatus(StatusHTListener.class);
            }
            listener = null;
        }
    }

    class StatusHTListener extends Status implements StatusRunnable {

        public StatusHTListener(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamAttackInfo pba) {
                belongTo.getStatus(StatusHaiTuShouHu.class)
                        .orElseGet(() -> {
                            StatusHaiTuShouHu status = new StatusHaiTuShouHu(from, belongTo);
                            belongTo.addStatus(status);
                            return status;
                        }).reduce(pba.getAttackInfo());
            }
            return false;
        }
    }

    class StatusHaiTuShouHu extends Status implements Displayable {
        private static final String StatusName = "海图守护";

        private double record;

        public StatusHaiTuShouHu(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        @Override
        public void beforeDelete() {
            belongTo.lostHP(record);
        }

        @Override
        public String getDisplayText() {
            return StatusName + ((int) record);
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
