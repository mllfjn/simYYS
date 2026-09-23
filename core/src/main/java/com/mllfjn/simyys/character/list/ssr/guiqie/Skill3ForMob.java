package com.mllfjn.simyys.character.list.ssr.guiqie;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill3ForMob extends Skill {
    private static final String SkillName = "鬼影闪(怪物)";
    private static final int[] multiplier = new int[]{0, 80, 84, 88, 92, 100};

    private final Skill2ForMob skill2;

    public Skill3ForMob(Character belongTo, int level, Skill2ForMob skill2) {
        super(belongTo, level, 3, 0, 3);
        this.skill2 = skill2;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive();
        Character target = new CharacterFinder(getBelongTo())
                .filterEnemy()
                .getPriorAuto(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);

        for (int i = 0; i < 3; i++) {
            interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        }

        skill2.attacked(interactive, target);

        return Optional.of(target);
    }
}
