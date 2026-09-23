package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase implements YingFenShenCopy {
    static final String SkillName = "破魔矢";
    private static final int[] baseMultiplier = new int[]{0, 100, 110, 120, 130, 140};

    private final int multiplier;

    public Skill1(Character belongTo, int level, int shuYin) {
        super(belongTo, level);
        this.multiplier = baseMultiplier[level] + 25 * shuYin;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    private void use(Character skillUser, Character target, int multiplier, Interactive interactive, boolean doLog) {
        interactive.attackTypical(this, target, multiplier, AttackType.DAN_TI);
        if (doLog) {
            log(skillUser, target);
        }
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        BoYa boYa = (BoYa) getBelongTo();
        use(boYa, target, multiplier, interactive, false);
        boYa.getSkill8().ifPresent(skill8 -> skill8.judgment(target));

        boYa.getYinFenShen().ifPresent(yfs -> yfs.usedSkill(this, target));
    }

    @Override
    public void copy(Character skillUser, Character target, Interactive interactive, int extraMultiplier) {
        use(skillUser, target, multiplier + extraMultiplier, interactive, true);
    }
}
