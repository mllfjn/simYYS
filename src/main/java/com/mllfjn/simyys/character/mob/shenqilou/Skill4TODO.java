package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill4TODO extends Skill {
    public static final String privateName = "钳连击";

    public Skill4TODO(Character belongTo) {
        super(belongTo, 0, 3, 4);
    }

    @Override
    public int getSkillID() {
        return 4;
    }

    @Override
    public String getName() {
        return privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getHit(bp);
        for (int i = 0; i < 4; i++) {
            interactive.attack(privateName, CharacterFinder.findEnemy(getBelongTo(), bp.characters), 100, AttackType.QUN_TI);
        }
    }
}
