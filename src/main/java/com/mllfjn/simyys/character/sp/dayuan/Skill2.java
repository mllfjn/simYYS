package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill2 extends Skill {
    public static final String SKillName = "守缘刃";

    public Skill2(Character belongTo) {
        super(belongTo, 1, 1, 0, 2);
    }

    @Override
    public String getName() {
        return SKillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Optional<StatusCombined> os = getBelongTo().getStatus(StatusCombined.class);
        os.ifPresent(status -> {
            // 再次释放时，解除目标 胜天之缘
            status.from.removeStatus(StatusSTChi.class);
            status.from.removeStatus(StatusSTQing.class);
            // 并驱散其全部减益状态与 TODO 控制效果
            status.from.dispelAllDebuff();
            status.delete();
        });
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && !getBelongTo().isHaveStatus(StatusCombined.class);
    }
}
