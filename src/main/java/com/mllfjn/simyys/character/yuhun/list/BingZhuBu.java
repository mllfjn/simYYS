package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.yuhun.Equip;

public class BingZhuBu extends Equip {
    public static final String EQUIP_NAME = "兵主部";

    @Override
    public String getName() {
        return EQUIP_NAME;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        new StatusBingRen(character);
    }

    private static class StatusBingRen extends Status {

        protected StatusBingRen(Character character) {
            super("兵刃", character);
            type(StatusType.BUFF, StatusForm.ZHUANG_TAI);
            enableStack(3);
            displayNameAndNonZeroStack();
            attribute(Attribute.IGNORE_DEFENCE, _ -> 75.0 * getStack());
            runOn(Trigger.AFTER_ROUND, _ -> addStack());
            retainAfterDie();
            retainAfterChangeWave(() -> {
                setStack(0);
                enableAction(Trigger.AFTER_ROUND);
            });
            addTo();
        }

        @Override
        public void addStack() {
            super.addStack();
            if (stack == 3) {
                disableAction(Trigger.AFTER_ROUND);
            }
        }
    }
}
