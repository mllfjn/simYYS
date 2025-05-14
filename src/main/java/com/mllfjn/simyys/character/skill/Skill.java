package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

public abstract class Skill {
    public String name;
    public Character belongTo;
    public final int level;
    public Skill(Character belongTo, int level) {
        this.belongTo = belongTo;
        this.level = level;
    }

    public abstract void setName();
    public abstract void use(BattlePane bp);
    public String getName() {
        return name;
    }
}
