package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.propertygetter.FlagChangeInfo;

public class LockSkillAndFlag {
    private Character character;

    private int lockSkill = -1;
    private FlagChangeInfo.FlagType flagType;
    private int flagTarget = -1;

    public LockSkillAndFlag(Character character) {
        this.character = character;
    }

    public void setLockSkill(int lockSkill) {
        this.lockSkill = lockSkill;
    }

    public void setFlag(FlagChangeInfo.FlagType flagType, int flagTarget) {
        this.flagType = flagType;
        this.flagTarget = flagTarget;
    }

    public int getLockSkill() {
        return lockSkill;
    }

    public FlagChangeInfo.FlagType getFlagType() {
        return flagType;
    }

    public int getFlagTarget() {
        return flagTarget;
    }

    public String getCharacterName() {
        return character.name;
    }

    public int getTeam() {
        return character.team;
    }

    public int getTimesToAct() {
        return character.timesToAct;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}
