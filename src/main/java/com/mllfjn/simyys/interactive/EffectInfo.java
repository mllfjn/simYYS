package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;

import java.util.function.BiFunction;

public class EffectInfo {
    private final Character from;
    private final Character target;
    private final Skill skill;

    private boolean isHit;
//    private boolean isResist;
private boolean isCancel;

    public EffectInfo(Character from, Character target, Skill skill) {
        this.from = from;
        this.target = target;
        this.skill = skill;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public Skill getSkill() {
        return skill;
    }
}
