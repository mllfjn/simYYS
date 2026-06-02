package com.mllfjn.simyys.character.list.sphudie;

import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.instance.StatusSleep;

import java.util.Optional;

public class SpHuDie extends CharacterShiShenBase {
    public static final String CharacterName = "梦引蝴蝶精";

    private Skill2 skill2;

    @Override
    public <T extends Status> Optional<T> addStatus(T newStatus) {
        if (newStatus instanceof StatusSleep) {
            skill2.use();
            return Optional.empty();
        } else {
            return super.addStatus(newStatus);
        }
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return false;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "";
    }

    @Override
    protected void addOwnSkills() {

    }
}
