package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusXinYang extends Status implements Displayable {
    private int stack;

    private StatusXinYang(Character character) {
        super(character, character, StatusType.BUFF, StatusForm.YIN_JI);
    }

    static void addStack() {
        // TODO 没写
    }

    @Override
    public String getDisplayText() {
        return "信仰" + stack;
    }
}
