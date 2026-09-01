package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;

class QiLingYueMoTu {
    static final String QiLingName = "月魔兔";

    static void install(Character character) {
        character.bp.addPriorityMove(character, () -> character.addStatus(new StatusQLYMTListener(character)));
    }

    static class StatusQLYMTListener extends Status {
        public StatusQLYMTListener(Character character) {
            super(QiLingName, character);
            attribute(Attribute.SPEED, 30);
            runOn(Trigger.AFTER_ROUND, _ -> {
                Character target = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .filterSelf()
                        .get(Attribute.LOCATION, CharacterFinder.Criteria.MAX);
                belongTo.doInteractive(interactive -> interactive.increaseLocation(target, 20));
            });
        }
    }
}
