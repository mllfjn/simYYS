package com.mllfjn.simyys.character.list.ssr.kongquemingwang;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1Special extends Skill1PuGongBase {
    private static final int[] MULTIPLIER = new int[]{0, 150, 160, 170, 180, 200};

    public Skill1Special(Character belongTo, int level) {
        super(belongTo, level, "流羽斩");
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.effect(target, 100, true, StatusJiaoXie.getSupplier(1));
        interactive.attackTypical(target, MULTIPLIER[getLevel()], AttackType.DAN_TI);
    }
}
