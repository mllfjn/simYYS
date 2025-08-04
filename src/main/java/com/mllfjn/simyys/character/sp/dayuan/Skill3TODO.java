package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.state.State;

class Skill3TODO extends Skill {
    public static final String privateName = "与世结缘";

    public Skill3TODO(Character belongTo, int level) {
        super(belongTo, level, 2, 0);
    }

    @Override
    public int getSkillID() {
        return 3;
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        // 目标首先是绿标，然后是结缘的式神，最后是攻击最高的
        Character target;
        if (bp.autoTo[getBelongTo().team] != null) {
            target = bp.autoTo[getBelongTo().team];
        } else if (getBelongTo().getState(StateFlagCombined.privateName) instanceof StateFlagCombined sfc){
            target = sfc.from;
        } else {
            target = CharacterFinder.find(bp.characters, getBelongTo().team, CharacterFinder.Property.ATTACK, CharacterFinder.Criteria.MAX);
        }
        lastUsedTarget = target;

        StateShenLi.addStack(getBelongTo(), 1);
    }
}
