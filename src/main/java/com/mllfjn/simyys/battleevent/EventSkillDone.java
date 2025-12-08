package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.skill.Skill;

public class EventSkillDone extends BattleEvent {
    private final Skill skill;

    public EventSkillDone(Skill skill) {
        this.skill = skill;
    }

    public Skill getSkill() {
        return skill;
    }
}
