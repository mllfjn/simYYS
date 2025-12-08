package com.mllfjn.simyys.character.list.mob.jifengmo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;

public class CiTiao7JiXing {
    public static final String CiTiaoName = "疾行";

    public static void install(Character character) {
        character.bp.addActionListener(character, event -> {
            if (event instanceof EventBattleStart) {
                List<Character> targets = new CharacterFinder(character)
                        .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                        .getList();

                for (Character target : targets) {
                    target.addStatus(new StatusJXListener(character, target));
                }
                return true;
            }
            return false;
        });
    }

    static class StatusJXListener extends Status implements InfluenceDamageWhenAttack, Runnable {
        private boolean increase = false;

        public StatusJXListener(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void doInfluenceWhenAttack(AttackType attackType, AttackInfo attackInfo) {
            // 造成伤害时，每有1个增益状态
            int count = 0;
            for (Status status : belongTo.getStatuses()) {
                if (status.statusType == StatusType.BUFF) {
                    count++;
                    // 上限5层
                    if (count == 5) {
                        break;
                    }
                }
            }

            if (count > 0) {
                // 提升4%
                attackInfo.getTraceableNumber().mul(1 + 0.04 * count, CiTiaoName);
                // 回合结束后增加自身20%行动条
                increase = true;
            }
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return increase && trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            belongTo.doInteractive(interactive -> interactive.increaseLocation(belongTo, 20));
            increase = false;
            return false;
        }
    }
}
