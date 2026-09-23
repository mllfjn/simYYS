package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;

import java.util.ArrayList;
import java.util.List;

class StatusDaYao extends Status implements IgnoreDebuff {
    private static final String StatusName = "大妖姿态";

    private final double originalMaxHp;
    private final List<Character> huanHuaList = new ArrayList<>(3);

    private final boolean isIncreaseMaxHP;

    // 受到致命伤害,在下一次行动结束时回到普通形态
    private boolean die = false;

    StatusDaYao(GeYe character, int initTeammateCount, int skillLevel) {
        super(StatusName, character);

        isIncreaseMaxHP = skillLevel >= 2;

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

        beforeDelete(() -> {
            belongTo.setMaxHp(originalMaxHp, true);
            belongTo.removeSkill(4);
            belongTo.addSkill(new Skill4(belongTo, ((GeYe) belongTo).skill3Level));
            for (Character c : huanHuaList) {
                c.removeStatus(StatusHuanHua.class);
            }
        });

        display(() -> StatusName + getStack());

        runOn(Trigger.OUT_ROUND_ACTION, Trigger.BEFORE_ROUND, _ ->
                belongTo.bp.gainGuiHuo(belongTo, getStack())
        );

        preventDie(_ -> {
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
        });

        if (skillLevel >= 3) {
            attribute(Attribute.ATTACK, _ -> belongTo.getInitAttack() * getStack() * 0.2);
        }

        if (skillLevel >= 4) {
            attribute(Attribute.DEFENCE, _ -> belongTo.getInitDefense() * getStack() * 0.2);
        }

        if (skillLevel >= 5) {
            attribute(Attribute.CRIT_POWER, _ -> 20.0 * getStack());
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
}
