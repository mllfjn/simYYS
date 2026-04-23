package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

public class CiTiao2YiSui {
    public static final String CiTiaoName = "易碎";

    public static void install(Character character) {
        character.addStatus(new StatusYiSui(character));
    }

    static class StatusYiSui extends Status implements AttributeModifier, StatusRunnable {
        // 自身降低35%的防御
        public StatusYiSui(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return -0.35 * belongTo.getInitDefense();
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEING_ATTACKED;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            // 首领受到的间接伤害降低99%
            if (attackInfo.getAttackType() == AttackType.JIAN_JIE) {
                attackInfo.getTraceableNumber().mul(0.01, CiTiaoName);
            }

            return false;
        }
    }
}
