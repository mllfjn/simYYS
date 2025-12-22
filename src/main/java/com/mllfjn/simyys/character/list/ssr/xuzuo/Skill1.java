package com.mllfjn.simyys.character.list.ssr.xuzuo;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    public static final String SkillName = "雷霆式";

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, multiplierGeneral[getLevel()], AttackType.DAN_TI);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
