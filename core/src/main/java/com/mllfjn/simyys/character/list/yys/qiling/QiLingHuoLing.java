package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;

public class QiLingHuoLing {
    static final String QiLingName = "火灵";
    private static final Skill SKILL = Skill.getInstance("火灵之力");

    public static void install(Character character) {
        character.bp.addPriorityMove(character, () -> character.bp.gainGuiHuo(character, 2));
        Status.of(QiLingName + "回合结束监听", character)
                .runOn(Trigger.AFTER_ROUND_FIRST, _ -> {
                    Interactive interactive = character.getInteractive();
                    List<Character> targets = new CharacterFinder(character)
                            .filterEnemy()
                            .getList();
                    for (int i = 0; i < 3; i++) {
                        interactive.attackTypical(SKILL, targets, 100, AttackType.QUN_TI);
                    }
                }).addTo();
    }
}
