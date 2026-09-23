package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "雪立·裁";

    private final StatusWLXR statusWLXR;

    public Skill1(Character belongTo, int level, StatusWLXR statusWLXR) {
        super(belongTo, level);
        this.statusWLXR = statusWLXR;
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        super.usePrivate(interactive, target);
        statusWLXR.addStack();
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
