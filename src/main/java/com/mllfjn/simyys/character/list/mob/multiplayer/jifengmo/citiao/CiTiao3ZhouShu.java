package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

public class CiTiao3ZhouShu {
    public static final String CiTiaoName = "咒术";

    public static void install(Character character) {
        character.addStatus(new StatusZhouShu(character));
    }

    static class StatusZhouShu extends Status implements StatusRunnable {

        public StatusZhouShu(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEING_ATTACKED;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            // 传导伤害增加30%
            if (attackInfo.getAttackType() == AttackType.CHUAN_DAO) {
                attackInfo.getTraceableNumber().mul(1.3, CiTiao3ZhouShu.CiTiaoName);
            }

            return false;
        }
    }
}
