package com.mllfjn.simyys.character.list.ssr.yunwaijing;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamStatus;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

class JingYouStore implements Serializable {
    private static final int MAX_CAPACITY = 2;

    private final Character owner;
    private final Skill3Yang skill;

    private StatusJingYou self;
    private final Deque<StatusJingYou> deque = new ArrayDeque<>(MAX_CAPACITY);

    private final BattleActionListener listener;
    private boolean added;

    JingYouStore(Character owner, Skill3Yang skill) {
        this.owner = owner;
        this.skill = skill;
        listener = new BattleActionListener(owner) {
            @Override
            public boolean onBattleAction(BattleEvent event) {
                if (event instanceof EventActionDone) {
                    doDispel();
                }
                return false;
            }
        };
    }

    void addJingYou(Character target) {
        if (target == owner) {
            // 给自己的,不占用友方上限
            if (self != null) {
                self.delete();
            }
            self = new StatusJingYou(owner, target);
        } else {
            // 友方最多2个
            if (deque.size() == MAX_CAPACITY) {
                deque.pollFirst().delete();
            }
            deque.addLast(new StatusJingYou(owner, target));
        }

        if (!added) {
            owner.bp().addActionListener(listener);
            added = true;
        }
    }

    private void remove(StatusJingYou jingYou) {
        if (jingYou == self) {
            self = null;
        } else {
            deque.remove(jingYou);
        }

        if (self == null && deque.isEmpty()) {
            owner.bp().removeActionListener(listener);
            added = false;
        }
    }

    private void doDispel() {
        if (self != null) {
            self.onDispel();
        } else {
            for (StatusJingYou statusJingYou : deque) {
                statusJingYou.onDispel();
            }
        }
    }

    void removeAll() {
        self = null;
        deque.clear();
        owner.bp().removeActionListener(listener);
        added = false;
    }

    private class StatusJingYou extends Status {
        private boolean dispel;

        private StatusJingYou(Character from, Character belongTo) {
            super("镜佑", from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);

            displayName();
            beforeDelete(() -> {
                JingYouStore.this.remove(this);
                belongTo.addStatus(new StatusJingDun(from, belongTo));
            });
            runOn(Trigger.ADDED_STATUS, param -> {
                if (((ParamStatus) param).getStatus().statusType == StatusType.DEBUFF) {
                    dispel = true;
                    disableAction(Trigger.ADDED_STATUS);
                }
            });
            addTo();
        }

        private void onDispel() {
            if (dispel) {
                belongTo.dispelDeBuff(skill.dispelCount);
                from.doInteractive(interactive -> {
                    interactive.increaseLocation(belongTo, skill.increase);
                    interactive.recovery(skill, belongTo, from.getDefence() * skill.recovery);
                });

                dispel = false;
                enableAction(Trigger.ADDED_STATUS);
            }
        }

    }
}
