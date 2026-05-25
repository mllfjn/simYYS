package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class ZhenMuShou extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "镇墓兽";

    private StatusZMSListener status;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = new StatusZMSListener(character);
    }

    @Override
    public void enable() {
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
    }

    class StatusZMSListener extends Status implements StatusRunnable {

        public StatusZMSListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.HP_CHANGE;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            belongTo.getStatus(StatusZMS.class).orElseGet(() -> {
                StatusZMS statusZMS = new StatusZMS(belongTo);
                belongTo.addStatus(statusZMS);
                return statusZMS;
            }).percentage = (100 - Math.ceil(100 * (belongTo.getHp() / belongTo.getMaxHp()))) * 0.005;
            ZhenMuShou.this.yuHunEffect();

            return false;
        }
    }

    static class StatusZMS extends Status implements AttributeModifier {
        private boolean counting;

        private double percentage;

        public StatusZMS(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return !counting && attribute == Attribute.CRIT_POWER;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            counting = true;
            double rt = belongTo.getCritPower() * percentage;
            counting = false;
            return rt;
        }
    }
}
