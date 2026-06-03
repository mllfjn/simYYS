package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;

class Skill3 extends PassiveSkill {
    private static final String SkillName = "皮糙肉厚";

    public Skill3(Character belongTo, double breakDamage) {
        super(belongTo, -1, 3);
        belongTo.addStatus(new StatusPiCaoRouHou(belongTo, breakDamage));
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
