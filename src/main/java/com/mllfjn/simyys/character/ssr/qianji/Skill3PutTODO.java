package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

public class Skill3PutTODO extends Skill {
    public static final String privateName = "海潮入梦";

    public Skill3PutTODO(Character belongTo, int level) {
        super(belongTo, level, 0, 0);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {

    }
}
