package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamLocationChange;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SKillName = "守缘刃";

    public Skill2(Character belongTo) {
        super(belongTo, 1, 1, 0, 2);
    }

    public static void addStatus(Character belongTo) {
        // 免疫来源于其他目标的行动条改变效果
        Status.of(SKillName + "免疫行动条改变", belongTo)
                .runOn(Trigger.LOCATION_WILL_CHANGE, triggerParam -> {
                    ParamLocationChange param = (ParamLocationChange) triggerParam;
                    if (param.isFromDecrease || param.isFromIncrease) {
                        if (param.from != belongTo) {
                            param.cancel();
                        }
                    }
                }).addTo();
    }

    @Override
    public String getName() {
        return SKillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        getBelongTo().getStatus(StatusCombined.class).ifPresent(status -> {
            // 再次释放时，解除目标 胜天之缘
            status.from.removeStatus(StatusSTChi.class);
            status.from.removeStatus(StatusSTQing.class);
            // 并驱散其全部减益状态与控制效果
            status.from.dispelAllDebuff();
            status.delete();
        });
        return Optional.empty();
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && getBelongTo().isHaveStatus(StatusCombined.class);
    }
}
