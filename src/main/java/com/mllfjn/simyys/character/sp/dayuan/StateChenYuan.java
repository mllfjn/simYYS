package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.trigger.Trigger;

import java.util.List;

class StateChiTODO extends State implements Runnable {
    public static final String privateName = "尘缘·赤";

    public StateChiTODO(DaYuan from, Character belongTo) {
        super(from, belongTo, StateType.BUFF, StateForm.YIN_JI);
        setSettleType(StateSettleType.CHI_XU, 1);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND;
    }

    @Override
    public void run(Trigger trigger, BattlePane bp) {
        // 无法连续触发此效果
        if (!belongTo.isHaveState(StateFlagNewRound.privateName)) {
            // 回合结束后立即获得1个回合
            bp.getNewRound(belongTo);
            belongTo.addState(new StateFlagNewRound((DaYuan) from, belongTo));
        }
    }

}

// 该状态标记目标处于尘缘·赤获得的新回合中,新回合释放妖术技能消耗的鬼火减少2点
class StateFlagNewRound extends State implements ReduceCost{
    public static final String privateName = "尘缘·赤新回合";

    public StateFlagNewRound(DaYuan from, Character belongTo) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public int getReduce() {
        return 2;
    }
}

class StateQingTODO extends State implements Runnable {
    public static final String privateName = "尘缘·青";

    public StateQingTODO(Character from, Character belongTo) {
        super(from, belongTo, StateType.BUFF, StateForm.YIN_JI);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND;
    }

    @Override
    public void run(Trigger trigger, BattlePane bp) {
        // 回合开始时驱散自身以外的全体友方1个减益状态与控制效果
        List<Character> teammate = CharacterFinder.findTeammate(belongTo, bp.characters);
        teammate.remove(belongTo);
        for (Character character : teammate) {
            character.dispelDeBuff(1);
        }
        // 并使自身减伤提升50%
    }
}

class StateQingJianShang extends State implements AttributeModifier {
    public static final String privateName = "尘缘·青减伤";

    public StateQingJianShang(DaYuan from, Character belongTo) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.JIAN_SHANG;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return 50;
    }

    @Override
    public void setName() {
        name = privateName;
    }
}