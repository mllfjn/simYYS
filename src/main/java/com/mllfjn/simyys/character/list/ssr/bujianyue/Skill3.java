package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

public class Skill3 extends Skill {
    private static final String SkillName = "水宿山行";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }
}
