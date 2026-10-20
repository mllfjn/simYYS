package com.mllfjn.simyys.character.list.ssr.xiaoye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "凝结之珀";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        XiaoYe belongTo = (XiaoYe) getBelongTo();
        int level = getLevel();
        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);

        int duration;
        if (level >= 4 && target.isMob()) {
            duration = 0;
        } else if (level >= 2) {
            duration = 2;
        } else {
            duration = 1;
        }

        StatusYiSui.add(belongTo, target, duration);

        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();
        Interactive interactive = belongTo.getInteractive();
        double increase = level >= 5 ? 20 : level >= 3 ? 15 : 10;
        for (Character character : list) {
            interactive.increaseLocation(character, increase);
        }

        return Optional.of(target);
    }
}
