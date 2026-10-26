package com.mllfjn.simyys.character.list.ssr.yunwaijing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3Yang extends Skill {
    private final JingYouStore jingYouStore;

    final int dispelCount;
    final int increase;
    final double recovery;

    public Skill3Yang(Character belongTo, int level) {
        super(belongTo, level, "苦海浮生", 3, 0, 3);
        this.recovery = level >= 2 ? 4.8 : 2.4;
        this.dispelCount = level >= 3 ? 2 : 1;
        this.increase = level >= 4 ? 20 : 10;

        jingYouStore = new JingYouStore(belongTo, this);
    }

    @Override
    protected int getCost() {
        return super.getCost() - getBelongTo().getLostHpPercentage() / 30;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character target = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .getAutoOrElseRandom();

        jingYouStore.addJingYou(target);

        List<Character> teammate = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .getList();

        Interactive interactive = getBelongTo().getInteractive(this);
        for (Character character : teammate) {
            interactive.increaseLocation(character, 20);
        }

        return Optional.of(target);
    }

    void changeToYang() {
        jingYouStore.addJingYou(getBelongTo());
    }

    void changeToYin() {
        jingYouStore.removeAll();
    }
}
