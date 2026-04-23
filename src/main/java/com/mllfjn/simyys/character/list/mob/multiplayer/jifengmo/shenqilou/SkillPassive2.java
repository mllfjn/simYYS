package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkillCanNotSeal;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

class SkillPassive2 extends PassiveSkillCanNotSeal {
    private static final String SkillName = "蜃气升腾";

    public SkillPassive2(Character belongTo) {
        super(belongTo, 0, 6);
        belongTo.addStatus(new StatusBeAttack(belongTo));
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusBeAttack extends Status implements StatusRunnable {
        private int count = 0;

        public StatusBeAttack(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (trigger == Trigger.AFTER_ATTACK) {
                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                double number = attackInfo.getTraceableNumber().getNumber();
                if (!attackInfo.isCrit() && number > 0) {
                    // 蜃气楼每受到3次非暴击伤害，获得[蜃雾笼罩]
                    count++;
                    if (count == 3) {
                        count = 0;
                        StatusShenWuHuDun.get(belongTo);
                    }
                }
            }
            return false;
        }
    }
}
