package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.instance.StatusShield;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "智火长明";

    private boolean usePriority;

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 3);
        if (level >= 5) {
            belongTo.bp.addPriorityMove(belongTo, () -> {
                usePriority = true;
                useWithoutCost();
                usePriority = false;
            });
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

        Character target = getTarget();

        target.replaceStatus(new StatusZSZH(belongTo, target, level >= 3 ? 60 : 30, belongTo.qm));

        if (level >= 2) {
            belongTo.addStatus(new StatusShield(belongTo, belongTo, lostHp));
            if (level >= 4) {
                belongTo.bp.gainGuiHuo(belongTo, 3);
            }
        }

        return Optional.of(target);
    }

    private Character getTarget() {
        CharacterFinder characterFinder = new CharacterFinder(getBelongTo())
                .filterTeammate();

        if (usePriority) {
            return characterFinder
                    .filterSelf()
                    .filterShiShen()
                    .get(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
        } else {
            return characterFinder
                    .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
        }
    }

}
