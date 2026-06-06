package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;
import java.util.Optional;

public abstract class Skill3Base extends Skill {
    public Skill3Base(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        DaShe belongTo = (DaShe) getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        List<CharacterSheMo> sheMoList = belongTo.getSheMoList();
        List<Character> choose = null;
        if (sheMoList != null && !sheMoList.isEmpty()) {
            choose = RateController.choose("蛇魔附加五感尽失", list, c -> c.name,
                    belongTo.getBp().calc, sheMoList.size()
            );
            for (Character character : choose) {
                character.addStatus(new StatusWGJS(belongTo, character));
            }
        }

        doUnique(interactive, list);

        if (choose != null) {
            for (int i = 0; i < getCommandSheMoTimes(); i++) {
                for (CharacterSheMo characterSheMo : sheMoList) {
                    List<Character> currentList = new CharacterFinder(belongTo)
                            .filterEnemy()
                            .getList();
                    characterSheMo.skillDuYe.attack(interactive, RateController
                            .choose("蛇魔攻击目标", currentList, c -> c.name, bp.calc)
                    );
                }
            }

            for (Character character : choose) {
                character.removeStatus(StatusWGJS.class);
            }
        }

        return Optional.empty();
    }

    abstract void doUnique(Interactive interactive, List<Character> list);

    abstract int getCommandSheMoTimes();
}
