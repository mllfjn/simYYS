package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.List;

class StatusDaYao extends Status implements Displayable, StatusRunnable, PreventDie, IgnoreDebuff, AttributeModifier {
    private static final String StatusName = "大妖姿态";

    private final double originalMaxHp;
    private final List<Character> huanHuaList;

    private final boolean isIncreaseAttack;
    private final boolean isIncreaseDefense;
    private final boolean isIncreaseCritPower;

    private final int stack;
    // 受到致命伤害,在下一次行动结束时回到普通形态
    private boolean die = false;

    private StatusDaYao(GeYe character, int initTeammateCount, List<Character> huanHuaList, int skillLevel) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.huanHuaList = huanHuaList;

        isIncreaseAttack = skillLevel >= 3;
        isIncreaseDefense = skillLevel >= 4;
        isIncreaseCritPower = skillLevel >= 5;

        stack = huanHuaList.size();

        originalMaxHp = character.getMaxHp();
        double baseMaxHp = character.getInitAttack() * 2.25;
        if (skillLevel >= 2) {
            baseMaxHp += originalMaxHp * 0.2 * stack;
        }
        character.setMaxHp(baseMaxHp, true);

        removeOtherStatus();
        character.removeSkill(4);
        character.addSkill(new Skill4Special(character, initTeammateCount));
    }

    static void install(GeYe character, int initTeammateCount, List<Character> huanHuaList, int skillLevel) {
        character.addStatus(new StatusDaYao(character, initTeammateCount, huanHuaList, skillLevel));
        /*character.getStatus(StatusDaYao.class).ifPresentOrElse(
                status -> status.stack = newStack,
                () -> {
                    StatusDaYao status = new StatusDaYao(character, initTeammateCount);
                    status.stack = newStack;
                    character.addStatus(status);
                }
        );*/
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
        for (Character character : huanHuaList) {
            character.removeStatus(StatusHuanHua.class);
        }
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

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return isIncreaseAttack && attribute == Attribute.ATTACK
                || isIncreaseDefense && attribute == Attribute.DEFENCE
                || isIncreaseCritPower && attribute == Attribute.CRIT_POWER;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        if (attribute == Attribute.ATTACK) {
            return belongTo.getInitAttack() * stack * 0.2;
        } else if (attribute == Attribute.DEFENCE) {
            return belongTo.getInitDefense() * stack * 0.2;
        } else {
            return 20 * stack;
        }
    }
}
