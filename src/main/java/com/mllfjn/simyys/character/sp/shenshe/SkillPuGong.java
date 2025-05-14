package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

public class SkillPuGong extends Skill {
    public static String privateName = "灵魂惩戒";
    public SkillPuGong(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void use(BattlePane bp) {
        Character target = CharacterFinder.findPriorAuto(bp, CharacterFinder.findEnemy(belongTo), CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);

    }

    @Override
    public void setName() {
        this.name = privateName;
    }
}
