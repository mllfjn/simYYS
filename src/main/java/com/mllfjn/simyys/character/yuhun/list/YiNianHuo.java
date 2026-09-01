package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class YiNianHuo extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "遗念火";
    public static final Class<? extends ConditionalReduceCost> clazz = StatusNianHuo.class;

    private StatusNianHuo status;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = new StatusNianHuo(character);
    }

    @Override
    public void enable() {
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
    }

    static class StatusNianHuo extends Status implements ConditionalReduceCost {
        public static final String StatusName = "念火";

        private int stack;

        public StatusNianHuo(Character character) {
            super(StatusName, character, character, StatusType.BUFF, StatusForm.YIN_JI);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return stack > 0 && attribute == Attribute.EFFECT_RESIST_RATE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return stack * 15;
        }

        @Override
        public int getMaxReduce() {
            return stack;
        }

        @Override
        public void enable(int usedCount) {
            stack -= usedCount;
        }

        private void stackChange() {
            display()
        }

        @Override
        public String getDisplayText() {
            if (stack == 0) {
                return null;
            }
            return StatusName + stack;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND && stack < 3;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            stack++;
            return false;
        }
    }
}
