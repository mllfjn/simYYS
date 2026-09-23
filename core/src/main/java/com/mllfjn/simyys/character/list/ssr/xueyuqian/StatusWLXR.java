package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusWLXR extends Status {
    private static final String StatusName = "巫灵雪刃";

    private int stack;

    public StatusWLXR(Character character) {
        super(StatusName, character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        display(() -> {
            if (stack > 0) {
                return StatusName + stack;
            } else {
                return null;
            }
        });
        retainAfterDie();
        retainAfterChangeWave(() -> stack = 0);
    }

    void addStack() {
        if (stack < 3) {
            stack++;
        }
    }

    boolean evolve() {
        if (stack >= 2) {
            stack -= 2;
            return true;
        } else {
            return false;
        }
    }
}
