package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.yuhun.Equip;

public class DaoHeSuiJian extends Equip {
    public static final String EQUIP_NAME = "稻荷穗箭";
    private static final Skill SKILL = Skill.getInstance(EQUIP_NAME);

    @Override
    public String getName() {
        return EQUIP_NAME;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        if (character.bp().isMobBattle(character)) {
            Character yys = new CharacterFinder(character)
                    .filterTeammate()
                    .filterYYS(true)
                    .getFirst();
            if (yys != null) {
                Status statusOnCharacter = Status.of(EQUIP_NAME, character);
                statusOnCharacter
                        .runOn(Trigger.AFTER_ROUND, _ -> {
                            character.doInteractive(SKILL, interactive ->
                                    interactive.increaseLocation(yys, 10)
                            );
                            statusOnCharacter.disableAction(Trigger.AFTER_ROUND);})
                        .retainAfterDie()
                        .retainAfterChangeWave()
                        .addTo();

                Status.of(EQUIP_NAME, character, yys)
                        .runOn(Trigger.BEFORE_ROUND, _ ->
                                statusOnCharacter.enableAction(Trigger.AFTER_ROUND))
                        .retainAfterDie()
                        .retainAfterChangeWave()
                        .addTo();
            }
        }
    }
}
