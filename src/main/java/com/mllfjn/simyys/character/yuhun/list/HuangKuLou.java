package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.character.yuhun.YuHunHitFeedBack;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;
import com.mllfjn.simyys.interactive.AttackInfo;

public class HuangKuLou extends YuHun implements YuHunUnfullMark, YuHunAttack, YuHunHitFeedBack {
    public static final String YuHunName = "荒骷髅";

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void effectInfo(AttackInfo attackInfo) {
        attackInfo.getTraceableNumber().mul(character.isHaveStatus(StatusHKLMark.class) ? 1.25 : 1.1, YuHunName);
        yuHunEffect();
    }

    @Override
    public void hitFeedBack(AttackInfo info) {
        StatusHKLMark.enable(character);
    }

    static class StatusHKLMark extends Status implements Displayable {
        private StatusHKLMark(Character character) {
            super(character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 1);
        }

        public static void enable(Character character) {
            if (character.isHaveStatus(StatusHKLMark.class)) {
                return;
            }

            character.addStatus(new StatusHKLMark(character));
        }

        @Override
        public String getDisplayText() {
            return HuangKuLou.YuHunName;
        }
    }
}
