package com.mllfjn.simyys.character.list.yys.yuanlaiguang;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    public static final String SkillName = "天剑";

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
        interactive.attackTypical(this, target
                , multiplierGeneral[getLevel()] + shuYin * 20, AttackType.DAN_TI);
    }

}
