package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;

public class CiTiao7JiXing {
    public static final String CiTiaoName = "疾行";

    public static void install(Character character) {
        character.bp.addPriorityMove(character, () -> {
            List<Character> targets = new CharacterFinder(character)
                    .filterEnemy()
                    .getList();

            for (Character target : targets) {
                target.addStatus(new StatusJXListener(character, target));
            }
        });
    }

    static class StatusJXListener extends Status implements InfluenceDamageWhenAttack, StatusRunnable {

        public StatusJXListener(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void doInfluenceWhenAttack(AttackInfo attackInfo) {
            // 造成伤害时，每有1个增益状态

            int count = getCount();
            if (count > 0) {
                // 提升4%
                attackInfo.getTraceableNumber().mul(1 + 0.04 * count, CiTiaoName);
            }
        }

        private int getCount() {
            int count = 0;
            for (Status status : belongTo.getStatuses()) {
                if (status.statusType == StatusType.BUFF) {
                    count++;
                    // 上限5层
                    if (count == 5) {
                        return 5;
                    }
                }
            }
            return count;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ACTION;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (getCount() == 5) {
                belongTo.doInteractive(interactive -> interactive.increaseLocation(belongTo, 20));
            }
            return false;
        }
    }
}
