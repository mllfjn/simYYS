package com.mllfjn.simyys.character.list.sp.huishihuaniaojuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.instance.StatusBiHu;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    public Skill1(Character belongTo, int level) {
        super(belongTo, level, "寒梅载雪");
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        super.usePrivate(interactive, target);
        Character belongTo = getBelongTo();
        if (getLevel() >= 5) {
            belongTo.addStatus(new StatusBiHu(belongTo, belongTo).duration(StatusDurationType.CHI_XU, 1));
        }
    }
}
