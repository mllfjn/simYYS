package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.RetainAfterChangeWave;
import com.mllfjn.simyys.character.status.determinant.RetainAfterDie;

class StatusWLXR extends Status implements Displayable, RetainAfterDie, RetainAfterChangeWave {
    private static final String StatusName = "巫灵雪刃";

    private int stack;
    private boolean beRemoved = false;

    public StatusWLXR(Character character) {
        super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
    }

    void addStack() {
        if (beRemoved) {
            belongTo.addStatus(this);
            beRemoved = false;
        }

        if (stack < 3) {
            stack++;
        }
    }

    @Override
    public void changeWaveAction() {
        stack = 0;
    }

    boolean evolve() {
        if (stack >= 2) {
            stack -= 2;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void beforeDelete() {
        stack = 0;
        beRemoved = true;
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }
}
