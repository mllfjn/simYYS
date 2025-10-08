package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

public class Skill2TODO extends Skill {
    public static final String privateName = "神堕之力";

    public Skill2TODO(Character belongTo, int level) {
        super(belongTo, level, 0, 0);
    }

    @Override
    public int getSkillID() {
        return 2;
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {

    }
}
