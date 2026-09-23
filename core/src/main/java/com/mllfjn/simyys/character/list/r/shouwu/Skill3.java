package com.mllfjn.simyys.character.list.r.shouwu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "虚无";
    private static final int[] multiplier = new int[]{0, 206, 216, 226, 236, 246};

    private final Skill2 skill2;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 3, 0, 3);
        this.skill2 = skill2;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .getPriorAuto(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
        Status status = Status.of(SkillName, belongTo);
        status.attribute(Attribute.IGNORE_DEFENCE, _ -> target.getDefence() * 0.4);
//        new StatusTemporarilyIgnoreDefense(belongTo, target);
        belongTo.addStatus(status);
        belongTo.getInteractive().attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        belongTo.removeStatus(status);

        skill2.madeAttack(target);

        return Optional.of(target);
    }
}
