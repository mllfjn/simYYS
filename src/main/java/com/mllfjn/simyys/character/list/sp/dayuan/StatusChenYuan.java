package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventRoundDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.List;

class StatusChi extends Status implements StatusRunnable, Displayable {
    static final String StatusName = "尘缘·赤";

    private final boolean isLv2;

    public StatusChi(DaYuan from, Character belongTo, int level) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        setDurationType(StatusDurationType.CHI_XU, 1);

        isLv2 = level >= 2;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND || trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        // 回合后立即获得1个回合,且
        if (trigger == Trigger.AFTER_ROUND) {
            // 无法连续触发此效果
            if (!belongTo.isHaveStatus(StatusChiNewRound.class)) {
                // 回合结束后立即获得1个回合,且新回合释放妖术技能消耗的鬼火减少2点(已包含在StatusChiNewRound中)
                belongTo.doInteractive(interactive -> interactive.getNewRound(belongTo));
//                belongTo.addStatus(new StatusChiNewRound((DaYuan) from, belongTo));

                belongTo.bp.addActionListener(belongTo, event -> {
                    if (event instanceof EventRoundDone) {
                        belongTo.addStatus(new StatusChiNewRound((DaYuan) from, belongTo));
                        return true;
                    }
                    return false;
                });

            }
        } else {
            // lv2 拥有尘缘的目标回合开始时立即获得1点鬼火
            if (isLv2) {
                belongTo.bp.gainGuiHuo(belongTo, 1);
            }
        }
        return false;
    }

    @Override
    public String getDisplayText() {
        // 同时附加赤和青会变成虹
        if (belongTo.isHaveStatus(StatusQing.class)) {
            return "尘缘·虹";
        } else {
            return StatusName;
        }
    }
}

// 该状态标记目标处于尘缘·赤获得的新回合中,新回合释放妖术技能消耗的鬼火减少2点
class StatusChiNewRound extends Status implements ForceChangeCost {
    public StatusChiNewRound(DaYuan from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        setDurationType(StatusDurationType.CHI_XU, 1);
    }

    @Override
    public int getChange() {
        return -2;
    }
}

class StatusQing extends Status implements StatusRunnable, Displayable {
    static final String StatusName = "尘缘·青";

    private final boolean isLv2;

    public StatusQing(DaYuan from, Character belongTo, int level) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        setDurationType(StatusDurationType.CHI_XU, 1);

        isLv2 = level >= 2;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        // 回合开始时驱散自身以外的全体友方1个减益状态与控制效果
        List<Character> teammate = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSelf()
                .getList();
        for (Character character : teammate) {
            character.dispelDeBuff(1);
        }
        // 并使自身减伤提升50%
        belongTo.addStatus(new StatusQingJianShang((DaYuan) from, belongTo));
        // lv2-回合开始时立即获得1点鬼火
        // 和赤一起加的时候由赤来回火
        if (isLv2 && !belongTo.isHaveStatus(StatusChi.class)) {
            belongTo.bp.gainGuiHuo(belongTo, 1);
        }
        return false;
    }

    @Override
    public String getDisplayText() {
        // 同时附加赤和青会变成虹，由赤来显示
        if (belongTo.isHaveStatus(StatusChi.class)) {
            return null;
        } else {
            return StatusName;
        }
    }
}

class StatusQingJianShang extends Status implements AttributeModifier {
    public StatusQingJianShang(DaYuan from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        from.addStatus(new StatusQingJianShangEnd(belongTo, this));
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.JIAN_SHANG;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return 50;
    }

    // 该状态在大缘身上，用于控制尘缘·青的减伤，在下回合开始时run，删除青的减伤
    private static class StatusQingJianShangEnd extends Status implements StatusRunnable {
        private final StatusQingJianShang qing;
        public StatusQingJianShangEnd(Character character, StatusQingJianShang qing) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.qing = qing;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            qing.delete();
            return true;
        }
    }
}