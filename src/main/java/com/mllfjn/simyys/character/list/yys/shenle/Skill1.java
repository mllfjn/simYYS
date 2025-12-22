package com.mllfjn.simyys.character.list.yys.shenle;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    public static final String SkillName = "伞击";
    private static final int[] multiplier = new int[]{0, 100, 110, 120, 130, 140};

    private final int shuYin;

    public Skill1(Character belongTo, int level, int shuYin) {
        super(belongTo, level);
        this.shuYin = shuYin;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        getBelongTo().getInteractive().attackTypical(this, target
                , multiplier[getLevel()] + 20 * shuYin, AttackType.DAN_TI);
    }
}
