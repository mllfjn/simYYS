package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

class StatusZhiKao extends Status {
    private int stack;

    private final BattleActionListener listener;
    private boolean added;

    public StatusZhiKao(Character from, Character belongTo, Skill2 skill2, int initStack) {
        super("炙烤", from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        stack = initStack;
        listener = new BattleActionListener(from) {
            @Override
            public boolean onBattleAction(BattleEvent event) {
                if (event instanceof EventActionDone) {
                    stack--;
                    if (from.team != belongTo.team) {
                        from.doInteractive(interactive -> {
                            AttackInfo attackInfo = new AttackInfo(from, skill2, belongTo, AttackType.DAN_TI, from.getDefence());
                            attackInfo.setMultiplier(300);
                            interactive.attack(attackInfo);
                        });
                    }
                    if (stack == 0) {
                        delete();
                    }
                    return true;
                }
                return false;
            }
        };

        runOn(Trigger.BEING_ATTACKED, param -> {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (CharacterFactory.isFireCharacter(attackInfo.getAttacker())) {
                attackInfo.getTraceableNumber().mul(1.16, getName());
                if (!added) {
                    added = true;
                    from.bp().addActionListener(listener);
                }
            }

        });

    }

    public static void addStack(Character from, Character belongTo, Skill2 skill2, int stack) {
        belongTo.addStatusOrChange(
                StatusZhiKao.class,
                statusZhiKao -> statusZhiKao.stack = Math.min(5, statusZhiKao.stack + stack),
                () -> new StatusZhiKao(from, belongTo, skill2, stack)
        );
    }
}
