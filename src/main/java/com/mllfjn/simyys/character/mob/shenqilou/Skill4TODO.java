package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill4TODO extends Skill {
    public static final String SkillName = "钳连击";

    public Skill4TODO(Character belongTo) {
        super(belongTo, 0, 3, 4, 4);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive();
        for (int i = 0; i < 4; i++) {
            interactive.attack(SkillName, CharacterFinder.findEnemy(getBelongTo(), bp.situation.characters), 100, AttackType.QUN_TI);
        }
    }
}
