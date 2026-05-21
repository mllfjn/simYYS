package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class StatusDaYao extends Status implements Displayable, StatusRunnable, PreventDie, IgnoreDebuff {
    private static final String StatusName = "大妖姿态";

    private final double originalMaxHp;

    private int stack;
    // 受到致命伤害,在下一次行动结束时回到普通形态
    private boolean die = false;

    private StatusDaYao(GeYe character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);

        originalMaxHp = character.getMaxHp();
        character.setMaxHp(character.getInitAttack() * 2.25, true);

        removeOtherStatus();
        character.removeSkill(4);
        character.addSkill(new Skill4Special(character));
    }

    static void install(GeYe character, int newStack) {
        character.getStatus(StatusDaYao.class).ifPresentOrElse(
                status -> status.stack = newStack,
                () -> {
                    StatusDaYao status = new StatusDaYao(character);
                    status.stack = newStack;
                    character.addStatus(status);
                }
        );
    }

    private void removeOtherStatus() {
        belongTo.getStatuses().removeIf(status ->
                (status.statusForm == StatusForm.ZHUANG_TAI || status.statusForm == StatusForm.YIN_JI)
                        && !(status instanceof StatusJiuWei));
    }

    int getStack() {
        return stack;
    }

    @Override
    public boolean ignoreDebuffEffective() {
        return belongTo.getHp() > (belongTo.getMaxHp() * 0.5);
    }

    @Override
    public void beforeDelete() {
        belongTo.setMaxHp(originalMaxHp, true);
        belongTo.removeSkill(4);
        belongTo.addSkill(new Skill4(belongTo, ((GeYe) belongTo).skill3Level));
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.BEFORE_ROUND) {
            belongTo.bp.gainGuiHuo(belongTo, stack);
        }
        return false;
    }

    @Override
    public void preventDie(double excessDamage) {
        if (!die) {
            belongTo.bp.addActionListener(belongTo, event -> {
                if (event instanceof EventActionDone) {
                    delete();
                    return true;
                }
                return false;
            });
            die = true;
        }
    }

    @Override
    public String getName() {
        return StatusName;
    }
}
