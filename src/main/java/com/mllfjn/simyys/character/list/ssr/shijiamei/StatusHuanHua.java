package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusHuanHua extends Status {
    private static final String StatusName = "幻花";

    private StatusHuanHua(Character character, int stack) {
        super(StatusName, character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        enableStack(5);
        setStack(stack);
        displayNameAndStack();
    }

    static void addStack(Character character, int stack) {
        character.getStatus(StatusHuanHua.class)
                .ifPresentOrElse(
                        status -> status.addStack(stack),
                        () -> character.addStatus(new StatusHuanHua(character, stack))
                );
    }

    @Override
    public void addStack(int amount) {
        if (stack < 5) {
            stack = Math.min(stack + amount, 5);
            if (stack == 5) {
                ((ShiJiaMei) belongTo).unlockDuanZui();
            }
        }
    }
}
