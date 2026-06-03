package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "智火长明";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 3);
        if (level >= 5) {
            belongTo.bp.addPriorityMove(belongTo, this::useWithoutCost);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        SiJinShen belongTo = (SiJinShen) getBelongTo();
        int level = getLevel();
        double lostHp = belongTo.getHp() * 0.24;
        belongTo.lostHP(lostHp);

        Character target = new CharacterFinder(belongTo)
                .filterTeammate()
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        target.replaceStatus(new StatusZSZH(belongTo, target, level >= 3 ? 60 : 30, belongTo.qm));

        if (level >= 2) {
            belongTo.addStatus(new StatusShield(belongTo, belongTo, lostHp));
            if (level >= 4) {
                belongTo.bp.gainGuiHuo(belongTo, 3);
            }
        }

        return Optional.of(target);
    }

}
