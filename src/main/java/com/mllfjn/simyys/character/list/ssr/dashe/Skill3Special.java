package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;

class Skill3Special extends Skill3Base {
    private static final String SkillName = "神愤之炎";

    public Skill3Special(Character belongTo) {
        super(belongTo, -1);
    }

    @Override
    void doUnique(Interactive interactive, List<Character> list) {
        interactive.attackTypical(this, list, 183, AttackType.QUN_TI);
    }

    @Override
    int getCommandSheMoTimes() {
        Character belongTo = getBelongTo();
        int guiHuoCount = belongTo.bp().getGuiHuoCount(belongTo);
        belongTo.bp().useGuiHuo(belongTo, guiHuoCount);
        return guiHuoCount;
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
