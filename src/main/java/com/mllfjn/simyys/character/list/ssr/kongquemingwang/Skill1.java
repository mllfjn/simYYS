package com.mllfjn.simyys.character.list.ssr.kongquemingwang;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    public Skill1(Character belongTo, int level) {
        super(belongTo, level, "流羽风");
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        super.usePrivate(interactive, target);
        interactive.effect(target, 30, true, StatusJiaoXie.getSupplier(1));
    }
}
