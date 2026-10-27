package com.mllfjn.simyys.character.list.sp.huishihuaniaojuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.HealInfo;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill3Special extends Skill {
    private static final int[] CATAPULTS = new int[]{0, 2, 2, 3, 3, 4};
    private static final int[] HEAL_MULTIPLIER = new int[]{0, 8, 10, 10, 12, 12};

    Skill3.CharacterFuShengHuaYing.StatusZhuMo statusZhuMo;

    public Skill3Special(Character belongTo, int level) {
        super(belongTo, level, "繁花坠露", 0, 0, 3);
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive(this);

        Character target = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSelf()
                .filterSummon(false)
                .getPriorAuto(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
        heal(interactive, target);

        Character lastTarget = target;
        for (int i = 0; i < CATAPULTS[getLevel()]; i++) {
            final Character lt = lastTarget;
            Character nextTarget = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .filterSummon(false)
                    .filter(c -> c != lt)
                    .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
            heal(interactive, nextTarget);
            lastTarget = nextTarget;
        }

        return Optional.of(target);
    }

    private void heal(Interactive interactive, Character target) {
        HealInfo healInfo = interactive.healTypical(target, HEAL_MULTIPLIER[getLevel()]);
        if (healInfo.isCrit()) {
            double number = healInfo.getTraceableNumber().getNumber();
            if (!statusZhuMo.belongTo.isMob()) {
                number *= 0.2;
            }
            statusZhuMo.attack(this, number);
        }
    }
}
