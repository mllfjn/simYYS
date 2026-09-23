package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;

class Skill3 extends Skill3Base {
    private static final String SkillName = "神念之影";
    private static final int[] multiplier = new int[]{0, 158, 166, 175, 183, 183};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    void doUnique(Interactive interactive, List<Character> list) {
        interactive.attackTypical(this, list, multiplier[getLevel()], AttackType.QUN_TI);
    }

    @Override
    int getCommandSheMoTimes() {
        return 1;
    }

    boolean needListener() {
        return getLevel() >= 5;
    }

    void convert() {
        getBelongTo().removeSkill(this);
        getBelongTo().addSkill(new Skill3Special(getBelongTo()), true);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
