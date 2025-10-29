package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1TODO extends Skill {
    public static final String privateName = "千汐";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 120, 125};
    public Skill1TODO(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 1);
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character target = CharacterFinder.findPriorAuto(bp, CharacterFinder.getEnemyTeam(getBelongTo()), CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);
        lastUsedTarget = target;

        Interactive interactive = getBelongTo().getInteractive(bp);
        interactive.attack(privateName, target, multiplier[getLevel()], AttackType.DAN_TI);

        //TODO +1潮声
    }
}
