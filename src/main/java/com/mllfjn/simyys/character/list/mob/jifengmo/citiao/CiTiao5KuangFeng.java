package com.mllfjn.simyys.character.list.mob.jifengmo.citiao;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.Optional;

public class CiTiao5KuangFeng {
    public static final String CiTiaoName = "狂风";

    public static void install(Character character) {
        character.addStatus(new StatusKFListener(character));
    }


    static class StatusKFListener extends Status implements StatusRunnable {

        public StatusKFListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamAfterAttack pa) {
                InteractiveInfo interactiveInfo = pa.interactiveInfo;
                Character attacker = interactiveInfo.getAttacker();
                // 自身（指玩家，这里是对面的队伍）回合内造成伤害时，额外对其造成真实伤害，伤害量等同于该次伤害的20%
                if (attacker.isInRound() && !(interactiveInfo.getSkill() instanceof SkillKF)) {
                    belongTo.doInteractive(interactive -> interactive
                            .attack(InteractiveInfo.createRealAttack(attacker, SkillKFInstance, belongTo
                                            , (c1, c2) -> interactiveInfo.getTraceableNumber().getNumber() * 0.2)
                                    , AttackType.ZHEN_SHI));
                }
            }
            return false;
        }
    }

    private final static SkillKF SkillKFInstance = new SkillKF();

    private static class SkillKF extends Skill {

        private SkillKF() {
            super(null, 0, 0, 0, 0);
        }

        @Override
        public String getName() {
            return CiTiaoName;
        }

        @Override
        public Optional<Character> usePrivate(BattlePane bp) {
            return Optional.empty();
        }
    }
}
