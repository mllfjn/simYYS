package com.mllfjn.simyys.character.list.yys.qingming;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.interactive.*;

class Skill1 extends Skill1PuGongBase {
    static final String SkillName = "基础术式";

    private final int multiplier;
    private final int rate;
    private final double num;

    public Skill1(Character belongTo, int level, int shuYin) {
        super(belongTo, level);

        multiplier = (level >= 5 ? 120 : 100) + shuYin * 10;

        if (level >= 2) {
            rate = level >= 3 ? 100 : 50;
            num = (level >= 4 ? 40 : 20) + shuYin * 6;
        } else {
            rate = 0;
            num = 0;
        }
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, multiplier, AttackType.DAN_TI);

        if (rate > 0) {
            interactive.effect(this, target, rate, true, StatusLuan.getSupplier(num));
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusLuan extends Status implements InfluenceDamageWhenAttack, Displayable {
        private static final String StatusName = "符咒·乱";

        private final double bonus;

        public StatusLuan(Character from, Character belongTo, double num) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            this.bonus = (100 - num) / 100;

            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        public static StatusSupplier getSupplier(double num) {
            return new StatusSupplier(StatusName, StatusLuan.class, (from, to) ->
                    to.getStatus(StatusLuan.class).ifPresentOrElse(
                            status -> {
                                if (status.getDuration() < 2) {
                                    status.setDuration(2);
                                }
                            },
                            () -> to.addStatus(new StatusLuan(from, to, num))
                    )
            );
        }

        @Override
        public String getDisplayText() {
            return StatusName + getDuration();
        }

        @Override
        public void doInfluenceWhenAttack(AttackInfo attackInfo) {
            attackInfo.getTraceableNumber().mul(bonus, StatusName);
        }
    }
}
