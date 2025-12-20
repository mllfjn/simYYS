package com.mllfjn.simyys.character.status.triggerParam;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

public class ParamUseSkill extends TriggerParam {
    private final Skill skill;
    private final Optional<Character> target;

    public ParamUseSkill(Skill skill, Optional<Character> target) {
        this.skill = skill;
        this.target = target;
    }

    public Skill getSkill() {
        return skill;
    }

    public Optional<Character> getTarget() {
        return target;
    }
}
