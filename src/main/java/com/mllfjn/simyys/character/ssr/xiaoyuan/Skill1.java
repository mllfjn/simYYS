package com.mllfjn.simyys.character.ssr.xiaoyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill {
    public static final String privateName = "月下花";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 115, 115};
    public Skill1(Character belongTo, int level) {
        super(belongTo, level, 0, 0);
    }

    @Override
    public int getSkillID() {
        return 1;
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character target = CharacterFinder.findPriorAuto(bp, CharacterFinder.getEnemyTeam(getBelongTo()), CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);
        lastUsedTarget = target;

        Interactive interactive = getBelongTo().getHit(bp);
        interactive.attack(privateName, target, multiplier[getLevel()], AttackType.DAN_TI);

        if (getLevel() >= 5) {
            getBelongTo().increaseLocation(bp, getBelongTo(), 20);
        }
    }
}
