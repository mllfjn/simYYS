package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventRoundDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkillCanNotSeal;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.ratecontroller.RateController;

class Skill3 extends PassiveSkillCanNotSeal {
    private static final String SkillName = "天罗地网";

    private final Skill4 skill4;

    public Skill3(Character belongTo, Skill4 skill4) {
        super(belongTo, -1, 3);
        this.skill4 = skill4;

        belongTo.addStatus(new StatusTLDWListener(belongTo));
    }

    @Override
    public String getSkillDesc() {
        return "√\t受到一定(?)伤害时,有35%概率立即释放一次天罗地网";
    }

    @Override
    public String getName() {
        return SkillName;
    }

    class StatusTLDWListener extends Status implements StatusRunnable {
        private boolean use = false;

        public StatusTLDWListener(Character c) {
            super(c, c, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return (!use && trigger == Trigger.AFTER_ATTACK);
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamAfterAttack) {
                if (RateController.otherWhether(SkillName, "使用", bp.calc, 35)) {
                    use = true;
                    belongTo.bp.addActionListener(belongTo, event -> {
                        if (event instanceof EventRoundDone) {
                            Skill3.this.skill4.useWithoutCost(belongTo.bp);
                            use = false;
                            return true;
                        }
                        return false;
                    });
                }
            }
            return false;
        }
    }
}
