package com.mllfjn.simyys.character.list.mob.jifengmo;

import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.ForceChangeCost;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

public class CiTiao4MengHuo {
    public static final String CiTiaoName = "猛火";

    public static void install(Character character) {
        character.bp.addActionListener(character, event -> {
            if (event instanceof EventBattleStart) {
                for (Character c : character.bp.situation.characters) {
                    c.addStatus(new StatusMHForceChange(character, c));
                }
                return true;
            }
            return false;
        });
    }

    static class StatusMHForceChange extends Status implements ForceChangeCost {

        public StatusMHForceChange(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public int getChange() {
            // 鬼火消耗降低2点
            return -2;
        }
    }
}
