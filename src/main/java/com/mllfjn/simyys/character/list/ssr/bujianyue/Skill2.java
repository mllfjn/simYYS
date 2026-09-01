package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "古山之神";

    private final int nonCritIncreaseDuration;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        if (level >= 2) {
            belongTo.addStatus(new StatusBeforeRoundGet(belongTo));
            if (level >= 4) {
                belongTo.bp.addPriorityMove(belongTo, () -> {
                    StatusYun.addStack(belongTo, this);
                    StatusShan.addStack(belongTo);
                });
            }
        }
        this.nonCritIncreaseDuration = level >= 5 ? 2 : 1;
    }

    int getNonCritIncreaseDuration() {
        return nonCritIncreaseDuration;
    }

    void getYunYi() {
        if (isActive()) {
            StatusYun.addStack(getBelongTo(), this);
        }
    }

    void getShanSe() {
        if (isActive()) {
            StatusShan.addStack(getBelongTo());
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    private class StatusBeforeRoundGet extends Status {
        private static final List<String> choose = List.of(StatusYun.StatusName, StatusShan.StatusName);

        public StatusBeforeRoundGet(Character character) {
            super(SkillName + "回合前监听", character);
            retainAfterDie();
            retainAfterChangeWave();
            runOn(Trigger.BEFORE_ROUND, _ -> {
                String result = RateController.choose("不见岳-回合前随机获得", choose, s -> s, belongTo.bp.calc);
                if (result.equals(choose.getFirst())) {
                    StatusYun.addStack(belongTo, Skill2.this);
                } else {
                    StatusShan.addStack(belongTo);
                }
            });
        }
    }
}
