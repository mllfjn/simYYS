package com.mllfjn.simyys.character.list.sp.sphongye;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.TraceableNumber;

class StatusLinYin extends Status {
    private static final String StatusName = "林隐";

    private final StatusAdder<?> adder;

    private final double beingJianJieAttack;
    private final double beingNormalAttack;

    private int stack = 2;

    private StatusLinYin(SPHongYe character, int level, Skill2 skill2) {
        super(StatusName, character);
        type(StatusType.BUFF, StatusForm.YIN_JI);
        display(() -> StatusName + stack);

        if (level >= 2) {
            attribute(Attribute.IGNORE_DEFENCE, 200.0);
        }

        beingJianJieAttack = level >= 4 ? 1.1 : 1.2;
        beingNormalAttack = level >= 4 ? 0.7 : 0.8;

        // 替换技能
        character.removeSkill(1);
        character.addSkill(new Skill1Special(character, character.skill1Level, this), true);

        // 友方获得叶之护
        adder = belongTo.bp.addStatusAdder(c ->
                c.team == belongTo.team
                        ? new StatusYeZhiHu(belongTo, c, level >= 3, skill2)
                        : null
        );

        beforeDelete(() -> {
            belongTo.removeSkill(1);
            belongTo.addSkill(new Skill1(belongTo, ((SPHongYe) belongTo).skill1Level), true);

            adder.deleteAndRemove();
        });
        runOn(Trigger.BEING_ATTACKED, param -> {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            TraceableNumber traceableNumber = attackInfo.getTraceableNumber();
            if (attackInfo.getAttackType() == AttackType.JIAN_JIE) {
                traceableNumber.mul(beingJianJieAttack, StatusName);
            } else {
                traceableNumber.mul(beingNormalAttack, StatusName);
            }
        });
    }

    void reduceStack() {
        if (stack == 1) {
            delete();
        } else {
            stack--;
        }
    }

    static void install(SPHongYe character, int level, Skill2 skill2) {
        character.getStatus(StatusLinYin.class)
                .ifPresentOrElse(
                        status -> status.stack = 2,
                        () -> character.addStatus(new StatusLinYin(character, level, skill2))
                );
    }

    static class StatusYeZhiHu extends Status {
        private boolean added;

        public StatusYeZhiHu(Character from, Character belongTo, boolean increaseDefense, Skill2 skill2) {
            super("叶之护", from, belongTo);
            type(StatusType.BUFF, StatusForm.YIN_JI);
            runOn(Trigger.AFTER_ATTACK, triggerParam -> {
                if (!added) {
                    belongTo.bp().addActionListener(new BattleActionListener(from) {
                        @Override
                        public boolean onBattleAction(BattleEvent event) {
                            StatusYeJin.addStack(
                                    from,
                                    ((ParamAttackInfo) triggerParam).getAttackInfo().getAttacker(),
                                    skill2
                            );
                            added = false;
                            return true;
                        }
                    });
                    added = true;
                }
            });
            if (increaseDefense) {
                attribute(Attribute.DEFENCE, 100.0);
            }
        }
    }
}
