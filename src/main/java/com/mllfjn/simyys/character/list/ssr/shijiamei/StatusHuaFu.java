package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

import java.util.Optional;

class StatusHuaFu extends Status {
    private static final String StatusName = "花祓";

    private StatusHuaFu(Character character, int stack) {
        super(StatusName, character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        enableStack(5);
        setStack(stack);
        displayNameAndStack();
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
}
