package com.mllfjn.simyys.character.list.ssr.yunwaijing;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1Yang extends Skill1PuGongBase {
    private static final int[] MULTIPLIER = new int[]{0, 200, 240, 280, 320, 360};

    public Skill1Yang(Character belongTo, int level) {
        super(belongTo, level, "昙无");
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        Character belongTo = getBelongTo();

        AttackInfo attackInfo = new AttackInfo(
                belongTo, this, target, AttackType.DAN_TI, belongTo.getDefence());
        attackInfo.setMultiplier(MULTIPLIER[getLevel()]);
        interactive.attack(attackInfo);

        Character character = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSelf()
                .get(Attribute.LOCATION, CharacterFinder.Criteria.MAX);
        interactive.increaseLocation(character, 20);
    }
}
