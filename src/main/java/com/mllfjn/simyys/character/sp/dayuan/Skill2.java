package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionIncrease;

import java.util.Optional;

class Skill2 extends Skill {
    public static final String SKillName = "守缘刃";

    public Skill2(Character belongTo) {
        super(belongTo, 1, 1, 0, 2);
    }

    public static void addStatus(Character belongTo) {
        // 免疫来源于其他目标的行动条改变效果
        belongTo.addStatus(new StatusIgnoreOtherActionChange(belongTo));
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
            // 并驱散其全部减益状态与 TODO 控制效果
            status.from.dispelAllDebuff();
            status.delete();
        });
        return Optional.empty();
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return super.canUse(bp) && !getBelongTo().isHaveStatus(StatusCombined.class);
    }

    static class StatusIgnoreOtherActionChange extends Status implements IgnoreActionIncrease, IgnoreActionDecrease {
        public StatusIgnoreOtherActionChange(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean effective(Character from) {
            return from != belongTo;
        }
    }
}
