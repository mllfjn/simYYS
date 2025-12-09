package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.status.determinant.RejectAllStatuses;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;

public class QingNvFang extends YuHun {
    public static final String YuHunName = "青女房";

    public static Class<? extends Status> getStatusClass() {
        return StatusQNFBF.class;
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character) {
        super.init(character);
        character.addStatus(new StatusQNFListener(character));
    }

    static class StatusQNFListener extends Status implements PreventDie {

        public StatusQNFListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean effective() {
            return !belongTo.isYuHunSeal();
        }

        @Override
        public void preventDie() {

            belongTo.bp.interactive.addYuHunEffectLog(belongTo, QingNvFang.YuHunName);

            // 受到致命伤害时,移除所有状态和印记
            belongTo.getStatuses().removeIf(status -> {
                if (status.statusForm == StatusForm.ZHUANG_TAI || status.statusForm == StatusForm.YIN_JI) {
                    status.beforeDelete();
                    return true;
                }
                return false;
            });
            // 恢复100%生命
//            belongTo.doInteractive(interactive -> interactive.recovery(belongTo, belongTo.getMaxHp()));
            // 因为游戏里触发没有显示,所以不用interactive直接改生命
            belongTo.setHp(belongTo.getMaxHp());
            // 并使自身冰封1回合
            belongTo.addStatus(new StatusQNFBF(belongTo));

            delete();
        }

        @Override
        public String getName() {
            return QingNvFang.YuHunName;
        }
    }

    static class StatusQNFBF extends Status implements AttributeModifier, RejectAllStatuses, StatusRunnable, Displayable {

        public StatusQNFBF(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            // 期间提升100%防御,免疫所有减益
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return belongTo.getInitDefense();
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            // 若冰封结束时仍存活,则再次恢复100%生命
            belongTo.doInteractive(
                    interactive -> interactive.recovery(Skill.getInstance(QingNvFang.YuHunName)
                            , belongTo, belongTo.getMaxHp()));
            return true;
        }

        @Override
        public String getText() {
            return "青女房冰封";
        }
    }
}
