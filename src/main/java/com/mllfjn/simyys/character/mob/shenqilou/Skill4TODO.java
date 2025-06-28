package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.hit.AttackType;
import com.mllfjn.simyys.hit.Hit;

class Skill4TODO extends Skill {
    public static final String privateName = "钳连击";

    public Skill4TODO(Character belongTo) {
        super(belongTo, 0, 3, 4);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Hit hit = new Hit(bp, getBelongTo());
        for (int i = 0; i < 4; i++) {
            hit.attack(privateName, CharacterFinder.findEnemy(getBelongTo(), bp.characters), 100, AttackType.QUN_TI);
        }
    }
}
