package com.mllfjn.simyys.character.list.sr.qingji;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusPoisoning;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.EffectInfo;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "蛇行击";
    private static final int[] multiplier = new int[]{0, 86, 90, 94, 98, 102, 102};

    private final Skill2 skill2;

    public Skill1(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level);
        this.skill2 = skill2;
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        EffectInfo effectInfo = interactive.effect(this, target, 100, true,
                StatusPoisoning.getSupplier(3, 5)
        );
        if (effectInfo.isHit()) {
            skill2.madePoisoning(target);
        }
        StatusFenHuo.install(getBelongTo(), target, this, getLevel() >= 6 ? 33 : 22);
    }

    @Override
    public String getName() {
        return SkillName;
    }

}
