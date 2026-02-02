package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;

public class QiLingHuoLing {
    public static final String QiLingName = "火灵";
    private static final Skill SKILL = Skill.getInstance("火灵之力");

    public static void install(Character character) {
        character.bp.atBattleStart(() -> character.bp.gainGuiHuo(character, 2));
        character.addStatus(new StatusQiHuoLing(character));
    }

    static class StatusQiHuoLing extends Status implements StatusRunnable {
        public StatusQiHuoLing(com.mllfjn.simyys.character.Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND_FIRST;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            Interactive interactive = belongTo.getInteractive();
            List<Character> targets = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .getList();
            for (int i = 0; i < 3; i++) {
                interactive.attackTypical(SKILL, targets, 100, AttackType.QUN_TI);
            }
            return false;
        }
    }
}
