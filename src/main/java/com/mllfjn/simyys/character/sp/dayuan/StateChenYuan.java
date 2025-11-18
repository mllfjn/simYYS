package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.trigger.Trigger;

import java.util.List;

class StateChi extends State implements Runnable, Displayable {
    public static final String StateName = "尘缘·赤";
    private final boolean isLv2;
    public StateChi(DaYuan from, Character belongTo, int level) {
        super(from, belongTo, StateType.BUFF, StateForm.YIN_JI);
        setSettleType(StateSettleType.CHI_XU, 1);

        isLv2 = level >= 2;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND || trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp) {
        // 回合后立即获得1个回合,且
        if (trigger == Trigger.AFTER_ROUND) {
            // 无法连续触发此效果
            if (!belongTo.isHaveState(StateChiNewRound.class)) {
                // 回合结束后立即获得1个回合,且新回合释放妖术技能消耗的鬼火减少2点(已包含在StateChiNewRound中)
                belongTo.doInteractive(interactive -> interactive.getNewRound(belongTo));
                belongTo.addState(new StateChiNewRound((DaYuan) from, belongTo));
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
    public String getText() {
        // 同时附加赤和青会变成虹
        if (belongTo.isHaveState(StateQing.class)) {
            return "尘缘·虹";
        } else {
            return StateName;
        }
    }
}

// 该状态标记目标处于尘缘·赤获得的新回合中,新回合释放妖术技能消耗的鬼火减少2点
class StateChiNewRound extends State implements ReduceCost {
    public StateChiNewRound(DaYuan from, Character belongTo) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        setSettleType(StateSettleType.CHI_XU, 1);
    }

    @Override
    public int getReduce() {
        return 2;
    }
}

class StateQing extends State implements Runnable, Displayable {
    public static final String StateName = "尘缘·青";
    private final boolean isLv2;
    public StateQing(DaYuan from, Character belongTo, int level) {
        super(from, belongTo, StateType.BUFF, StateForm.YIN_JI);
        setSettleType(StateSettleType.CHI_XU, 1);

        isLv2 = level >= 2;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp) {
        // 回合开始时驱散自身以外的全体友方1个减益状态与控制效果
        List<Character> teammate = CharacterFinder.findTeammate(belongTo, bp.situation.characters);
        teammate.remove(belongTo);
        for (Character character : teammate) {
            character.dispelDeBuff(1);
        }
        // 并使自身减伤提升50%
        belongTo.addState(new StateQingJianShang((DaYuan) from, belongTo));
        // lv2-回合开始时立即获得1点鬼火
        if (isLv2) {
            belongTo.bp.gainGuiHuo(belongTo, 1);
        }
        return false;
    }

    @Override
    public String getText() {
        // 同时附加赤和青会变成虹，由赤来显示
        if (belongTo.isHaveState(StateChi.class)) {
            return null;
        } else {
            return StateName;
        }
    }
}

class StateQingJianShang extends State implements AttributeModifier {
    public StateQingJianShang(DaYuan from, Character belongTo) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        from.addState(new StateQingJianShangEnd(belongTo, this));
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
    private static class StateQingJianShangEnd extends State implements Runnable {
        private final StateQingJianShang qing;
        public StateQingJianShangEnd(Character character, StateQingJianShang qing) {
            super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
            this.qing = qing;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp) {
            qing.delete();
            return true;
        }
    }
}