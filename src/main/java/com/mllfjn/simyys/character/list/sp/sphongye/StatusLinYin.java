package com.mllfjn.simyys.character.list.sp.sphongye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.TraceableNumber;

import java.util.List;

class StatusLinYin extends Status implements StatusRunnable, Displayable, AttributeModifier {
    private static final String StatusName = "林隐";

    private final boolean ignoreDefense;
    private final BattleActionListener listener;

    private final double beingJianJieAttack;
    private final double beingNormalAttack;

    private int stack = 2;

    private StatusLinYin(SPHongYe character, int level) {
        super(character, character, StatusType.BUFF, StatusForm.YIN_JI);
        this.ignoreDefense = level >= 2;

        beingJianJieAttack = level >= 4 ? 1.1 : 1.2;
        beingNormalAttack = level >= 4 ? 0.7 : 0.8;

        // 替换技能
        character.removeSkill(1);
        character.addSkill(new Skill1Special(character, character.skill1Level, this), true);

        // 友方获得叶之护
        listener = belongTo.bp.forEveryone(belongTo, c -> {
            if (c.team == belongTo.team) {
                c.addStatus(new StatusYeZhiHu(belongTo, c, level >= 3));
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

    static void install(SPHongYe character, int level) {
        character.getStatus(StatusLinYin.class)
                .ifPresentOrElse(
                        status -> status.stack = 2,
                        () -> character.addStatus(new StatusLinYin(character, level))
                );
    }

    @Override
    public void beforeDelete() {
        belongTo.removeSkill(1);
        belongTo.addSkill(new Skill1(belongTo, ((SPHongYe) belongTo).skill1Level), true);

        belongTo.bp.removeActionListener(listener);
        List<Character> list = new CharacterFinder(belongTo, true)
                .filterTeammate()
                .getList();
        for (Character character : list) {
            character.removeStatus(StatusYeZhiHu.class);
        }
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEING_ATTACKED;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
        TraceableNumber traceableNumber = attackInfo.getTraceableNumber();
        if (attackInfo.getAttackType() == AttackType.JIAN_JIE) {
            traceableNumber.mul(beingJianJieAttack, StatusName);
        } else {
            traceableNumber.mul(beingNormalAttack, StatusName);
        }
        return false;
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return ignoreDefense && attribute == Attribute.IGNORE_DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return 200;
    }

    static class StatusYeZhiHu extends Status implements StatusRunnable, AttributeModifier {
        private final boolean increaseDefense;

        public StatusYeZhiHu(Character from, Character belongTo, boolean increaseDefense) {
            super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
            this.increaseDefense = increaseDefense;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return false;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            return false;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return increaseDefense && attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 100;
        }
    }
}
