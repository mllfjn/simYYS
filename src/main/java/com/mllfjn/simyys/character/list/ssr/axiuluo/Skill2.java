package com.mllfjn.simyys.character.list.ssr.axiuluo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "天魔威压";
    private static final int[] suppress = new int[]{0, 9, 8, 7, 6, 6};

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);
    }

    StatusLiXing getStatusLiXing() {
        return new StatusLiXing(getBelongTo(), 0.01 * suppress[getLevel()], getLevel() >= 5);
    }

    @Override
    public boolean canUse(BattlePane bp) {
        // 不是被动的被动...
        return false;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }

}
