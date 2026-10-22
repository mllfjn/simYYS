package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusWLXR extends Status {
    private static final String StatusName = "巫灵雪刃";

    public StatusWLXR(Character character) {
        super(StatusName, character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        enableStack(3);
        display(() -> {
            int stack = getStack();
            if (stack > 0) {
                return StatusName + stack;
            } else {
                return null;
            }
        });
        retainAfterDie();
        retainAfterChangeWave(() -> setStack(0));
    }

    boolean evolve() {
        int stack = getStack();
        if (stack >= 2) {
            setStack(stack - 2);
            return true;
        } else {
            return false;
        }
    }
}
