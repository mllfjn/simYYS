package com.mllfjn.simyys.character.SP.dayuan;

import com.mllfjn.simyys.character.Character;

public class DaYuan extends Character {
    public static final String privateName = "纺愿缘结神";
    public DaYuan() {

    }

    @Override
    public void useFrontSkill() {
        this.addState(new ShengTianZhiYuan_Chi(this, this));
    }
}
