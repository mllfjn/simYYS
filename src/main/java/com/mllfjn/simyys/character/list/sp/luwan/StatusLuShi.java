package com.mllfjn.simyys.character.list.sp.luwan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.Optional;

class StatusLuShi extends Status implements AttributeModifier, Displayable, StatusRunnable {
    private static final String StatusName = "麓蚀";

    private boolean isCounting = false;

    private StatusLuShi(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
    }

    static void addStatus(Character from, Character belongTo) {
        Optional<StatusLuShi> oStatus = belongTo.getStatus(StatusLuShi.class);
        if (oStatus.isEmpty()) {
            belongTo.addStatus(new StatusLuShi(from, belongTo));
        }
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return !isCounting && attribute == Attribute.ATTACK;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        isCounting = true;
        double attack = belongTo.getAttack();
        isCounting = false;
        return -0.15 * attack;
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        belongTo.addStatus(new StatusLuYa(from, belongTo));
        return true;
    }

    static class StatusLuYa extends Status implements Displayable, CrowdControl, AttributeModifier, StatusRunnable {
        private static final String StatusName = "麓压";

        private boolean isCounting = false;

        public StatusLuYa(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return !isCounting && attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            isCounting = true;
            double speed = belongTo.getSpeed();
            isCounting = false;
            return -0.3 * speed;
        }

        @Override
        public String getDisplayText() {
            return StatusName;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            return true;
        }
    }
}
