package com.mllfjn.simyys.character.list.ssr.guiqie;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1ForMob extends Skill1PuGongBase {
    private static final String SkillName = "鬼斩(怪物)";
    private static final int[] multiplier = new int[]{0, 80, 84, 88, 92, 100};

    private final Skill2ForMob skill2;

    public Skill1ForMob(Character belongTo, int level, Skill2ForMob skill2) {
        super(belongTo, level);
        this.skill2 = skill2;
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        skill2.attacked(interactive, target);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
