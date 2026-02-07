package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunHitFeedBack;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;
import com.mllfjn.simyys.interactive.AttackInfo;

public class DiZhenNian extends YuHun implements YuHunUnfullMark, YuHunSealResponse, YuHunHitFeedBack {
    public static final String YuHunName = "地震鲶";

    private StatusDiZhenNian status;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        if (status == null) {
            status = new StatusDiZhenNian(character);
        }
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
    }

    @Override
    public void hitFeedBack(AttackInfo info) {
        status.tackEffect(info);
        yuHunEffect();
    }

    static class StatusDiZhenNian extends Status implements AttributeModifier {
        private int stack = 0;
        private boolean installed = false;

        public StatusDiZhenNian(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        public void tackEffect(AttackInfo info) {
            if (!installed && stack < 10) {
                info.getSkill().addSkillEndListener(() -> installed = false);
                installed = true;
                stack++;
            }
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.JIAN_SHANG
                    || attribute == Attribute.ZENG_SHANG;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            // 战斗开始时,获得60%减伤,每次受到伤害,将6%减伤转化为提升1.5%伤害,单次攻击内最多触发1次
            // 用stack表示触发次数
            // 减伤的数值就是 60 - 6 * stack
            // 增伤的数值就是 1.5 * stack
            if (attribute == Attribute.JIAN_SHANG) {
                return 60 - 6 * stack;
            } else {
                return 1.5 * stack;
            }
        }
    }
}
