package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

import java.util.Optional;

class StatusHuaFu extends Status implements Displayable {
    private static final String StatusName = "花祓";

    private int stack;

    private StatusHuaFu(Character character, int stack) {
        super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        this.stack = stack;
    }

    static void addStack(Character character, int stack) {
        character.getStatus(StatusHuaFu.class)
                .ifPresentOrElse(
                        status -> status.addStack(stack),
                        () -> character.addStatus(new StatusHuaFu(character, stack))
                );
    }

    static boolean consumeStack(Character from) {
        Optional<StatusHuaFu> oStatus = from.getStatus(StatusHuaFu.class);
        if (oStatus.isEmpty()) {
            return false;
        }

        StatusHuaFu status = oStatus.get();
        if (status.stack > 0) {
            status.stack--;
            StatusHuanHua.addStack(from, 1);
            return true;
        }
        return false;
    }

    private void addStack(int addStack) {
        if (stack < 5) {
            stack = Math.min(stack + addStack, 5);
        }
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }
}
