package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAttack;
import com.mllfjn.simyys.character.yuhun.YuHunHitFeedBack;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;
import com.mllfjn.simyys.interactive.AttackInfo;

public class HuangKuLou extends YuHun implements YuHunUnfullMark, YuHunAttack, YuHunHitFeedBack {
    public static final String YuHunName = "荒骷髅";

    private boolean enhanced = false;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void effectInfo(AttackInfo attackInfo) {
        attackInfo.getTraceableNumber().mul(enhanced ? 1.25 : 1.1, YuHunName);
        yuHunEffect();
    }

    @Override
    public void hitFeedBack(AttackInfo info) {
        if (!enhanced) {
            Status.of(YuHunName, character)
                    .type(StatusType.BUFF, StatusForm.ZHUANG_TAI)
                    .duration(StatusDurationType.CHI_XU, 1)
                    .display(HuangKuLou.YuHunName)
                    .beforeDelete(() -> enhanced = false)
                    .addTo();
            enhanced = true;
        }
    }
}
