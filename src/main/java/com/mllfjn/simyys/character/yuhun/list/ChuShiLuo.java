package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
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

    private static class StatusChuShiLuo extends Status {
        private static final String StatusName = "螺壳";

        public StatusChuShiLuo(Character character) {
            super(StatusName, character);
            retainAfterDie();
            retainAfterChangeWave(this::enable);

            runOn(Trigger.BEFORE_ATTACK, param -> {
                TraceableNumber traceableNumber = ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber();
                double limit = belongTo.getMaxHp() * 0.6;
                if (traceableNumber.getNumber() > limit) {
                    traceableNumber.set(limit, StatusName);
                    disable();
                }
            });
            runOnAndDisable(Trigger.BEFORE_ROUND, _ -> enable());
            display(StatusName);
        }

        private void enable() {
            enableAction(Trigger.BEFORE_ATTACK);
            disableAction(Trigger.BEFORE_ROUND);
            display(StatusName);
        }

        private void disable() {
            disableAction(Trigger.BEFORE_ATTACK);
            enableAction(Trigger.BEFORE_ROUND);
            stopDisplay();
        }
    }
}
