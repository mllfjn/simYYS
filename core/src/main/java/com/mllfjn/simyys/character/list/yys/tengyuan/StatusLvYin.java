package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusLvYin extends Status {
    private static final String StatusName = "律音";

    private int maxStack = 6;
    private int stack;
    private Skill6 skill6;
    private Skill7 skill7;

    public StatusLvYin(Character character) {
        super(StatusName, character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        display(() -> StatusName + stack);
    }

    void setSkill6(Skill6 skill6) {
        this.skill6 = skill6;
    }

    void setSkill7(Skill7 skill7) {
        this.skill7 = skill7;
    }

    int getStack() {
        return stack;
    }

    void Skill6setMaxStack() {
        this.maxStack = 10;
    }

    void addStack(int count) {
        int canAdd = Math.min(count, maxStack - stack);
        if (canAdd > 0) {
            stack += canAdd;
            if (skill6 != null) {
                skill6.getLvYin(canAdd, maxStack);
            }
        }
    }

    boolean canUse(int count) {
        return stack >= count;
    }

    void use(int count) {
        stack -= count;
        if (skill7 != null) {
            skill7.useLvYin(count);
        }
    }
}
