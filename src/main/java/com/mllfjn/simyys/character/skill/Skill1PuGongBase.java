package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventUsePuGong;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

public abstract class Skill1PuGongBase extends Skill {
    public Skill1PuGongBase(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 1);
    }

    public Character getTarget() {
        return new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MIN);
    }

    public abstract void usePrivate(BattlePane bp, Interactive interactive, Character target);

    @Override
    public final Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }

    @Override
    public void use(BattlePane bp) {
        Character target = getTarget();
        // 广播使用普攻
        bp.onTrigger(new EventUsePuGong(getBelongTo(), target));

        usePrivate(bp, getBelongTo().getInteractive(), target);

        useDone();
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return true;
    }
}
