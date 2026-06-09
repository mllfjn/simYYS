package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.RetainAfterChangeWave;
import com.mllfjn.simyys.character.status.determinant.RetainAfterDie;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAfterBeingAttack;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.interactive.TraceableNumber;

public class ChuShiLuo extends YuHun implements YuHunAfterBeingAttack {
    public static final String YuHunName = "出世螺";
    private static final Skill skill = Skill.getInstance(YuHunName);

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        character.addStatus(new StatusChuShiLuo(character));
    }

    @Override
    public void action(AttackInfo attackInfo, Interactive interactive) {
        interactive.recovery(skill, character, attackInfo.getTraceableNumber().getNumber() * 0.1);
    }

    private static class StatusChuShiLuo extends Status
            implements StatusRunnable, RetainAfterDie, RetainAfterChangeWave, Displayable {
        private static final String StatusName = "螺壳";

        boolean effective = true;

        public StatusChuShiLuo(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return (trigger == Trigger.BEFORE_ATTACK && effective)
                    || (trigger == Trigger.BEFORE_ROUND && !effective);
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.BEFORE_ROUND) {
                effective = true;
            } else {
                TraceableNumber traceableNumber = ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber();
                double limit = belongTo.getMaxHp() * 0.6;
                if (traceableNumber.getNumber() > limit) {
                    traceableNumber.set(limit, StatusName);
                    effective = false;
                }

            }
            return false;
        }

        @Override
        public String getDisplayText() {
            if (effective) {
                return StatusName;
            } else {
                return null;
            }
        }

        @Override
        public void changeWaveAction() {
            effective = true;
        }
    }
}
