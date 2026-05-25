package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusHuanHua extends Status implements Displayable {
    private static final String StatusName = "幻花";

    private int stack;

    private StatusHuanHua(Character character, int stack) {
        super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        this.stack = stack;
    }

    static void addStack(Character character, int stack) {
        character.getStatus(StatusHuanHua.class)
                .ifPresentOrElse(
                        status -> status.addStack(stack),
                        () -> character.addStatus(new StatusHuanHua(character, stack))
                );
    }

    private void addStack(int addStack) {
        if (stack < 5) {
            stack = Math.min(stack + addStack, 5);
            if (stack == 5) {
                ((ShiJiaMei) belongTo).unlockDuanZui();
            }
        }
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }
}
