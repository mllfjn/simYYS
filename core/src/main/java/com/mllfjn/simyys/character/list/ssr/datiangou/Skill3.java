package com.mllfjn.simyys.character.list.ssr.datiangou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "羽刃暴风";
    public static final int[] multiplier = new int[]{0, 65, 67, 69, 71, 75};

    private final Skill2 skill2;

    Skill3(Character belongTo, int level, Skill2 skill2) {
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
        Interactive interactive = belongTo.getInteractive();

        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        skill2.addStack(list.size());

        for (int i = 0; i < 4; i++) {
            interactive.attackTypical(this, list, multiplier[getLevel()], AttackType.QUN_TI);
        }
        return Optional.empty();
    }
}
