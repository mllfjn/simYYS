package com.mllfjn.simyys.character.status.triggerParam;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

public class ParamUseSkill extends TriggerParam {
    private final Skill skill;
    private final Character target;
    private final int cost;

    public ParamUseSkill(Skill skill, Character target, int cost) {
        this.skill = skill;
        this.target = target;
        this.cost = cost;
    }

    public Skill getSkill() {
        return skill;
    }

    public Optional<Character> getTarget() {
        return Optional.ofNullable(target);
    }

    public int getCost() {
        return cost;
    }
}
