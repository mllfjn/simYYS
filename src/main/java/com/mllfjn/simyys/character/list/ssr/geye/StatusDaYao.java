package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.ArrayList;
import java.util.List;

class StatusDaYao extends Status implements Displayable, StatusRunnable, PreventDie, IgnoreDebuff, AttributeModifier {
    private static final String StatusName = "大妖姿态";

    private final double originalMaxHp;
    private final List<Character> huanHuaList = new ArrayList<>(3);

    private final boolean isIncreaseMaxHP;
    private final boolean isIncreaseAttack;
    private final boolean isIncreaseDefense;
    private final boolean isIncreaseCritPower;

    // 受到致命伤害,在下一次行动结束时回到普通形态
    private boolean die = false;

    StatusDaYao(GeYe character, int initTeammateCount, int skillLevel) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);

        isIncreaseMaxHP = skillLevel >= 2;
        isIncreaseAttack = skillLevel >= 3;
        isIncreaseDefense = skillLevel >= 4;
        isIncreaseCritPower = skillLevel >= 5;

        originalMaxHp = character.getMaxHp();

        character.removeSkill(4);
        character.addSkill(new Skill4Special(character, initTeammateCount), true);

        if (!huanHuaList.isEmpty()) {
            for (Character next : huanHuaList) {
                next.removeAllDeBuff();
                next.addStatus(new StatusHuanHua(belongTo, next));
            }
            changeDone();
        }
    }

    List<Character> getHuanHuaList() {
        return huanHuaList;
    }

    void addHuanHua(Character add) {
        huanHuaList.add(add);
        add.removeAllDeBuff();
        add.addStatus(new StatusHuanHua(belongTo, add));
    }

    void removeHuanHua(Character remove) {
        huanHuaList.remove(remove);
        remove.removeStatus(StatusHuanHua.class);
    }

    void changeDone() {
        if (huanHuaList.isEmpty()) {
            delete();
        } else {
            belongTo.getStatuses().removeIf(status ->
                    (status.statusForm == StatusForm.ZHUANG_TAI || status.statusForm == StatusForm.YIN_JI)
                            && !(status instanceof StatusJiuWei));

            double baseMaxHp = belongTo.getInitAttack() * 2.25;
            if (isIncreaseMaxHP) {
                baseMaxHp += originalMaxHp * 0.2 * getStack();
            }
            belongTo.setMaxHp(baseMaxHp, true);
        }
    }

    int getStack() {
        return huanHuaList.size();
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
        return StatusName + getStack();
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.OUT_ROUND_ACTION || trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        belongTo.bp.gainGuiHuo(belongTo, getStack());
        return false;
    }

    @Override
    public void preventDie(double excessDamage) {
        if (!die) {
            belongTo.bp.addActionListener(new BattleActionListener(belongTo) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    if (event instanceof EventActionDone) {
                        delete();
                        return true;
                    }
                    return false;
                }
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
            return belongTo.getInitAttack() * getStack() * 0.2;
        } else if (attribute == Attribute.DEFENCE) {
            return belongTo.getInitDefense() * getStack() * 0.2;
        } else {
            return 20 * getStack();
        }
    }
}
