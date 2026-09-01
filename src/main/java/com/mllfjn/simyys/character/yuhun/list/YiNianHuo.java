package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
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
            runOn(Trigger.BEFORE_ROUND, _ -> {
                stack++;
                stackChange();
            });
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
            if (stack != 0) {
                display(StatusName + stack);
                attribute(Attribute.EFFECT_RESIST_RATE, 15 * stack);
            } else {
                stopDisplay();
                attribute(Attribute.EFFECT_RESIST_RATE, 0);
            }

            if (stack < 3) {
                enableAction(Trigger.BEFORE_ROUND);
            } else {
                disableAction(Trigger.BEFORE_ROUND);
            }
        }
    }
}
