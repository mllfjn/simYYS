package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.state.CrowdControl;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.state.determinant.InfluenceDamage;

public class StateChenLun extends State implements CrowdControl, InfluenceDamage {
    private static final String privateName = "沉沦";
    private final int level;
    public StateChenLun(NaMei from, Character belongTo, int level) {
        super(from, belongTo, StateType.DEBUFF, StateForm.ZHUANG_TAI);
        this.level = level;
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public boolean effective(AttackType attackType, Character character) {
        // 受到来自伊邪那美与处于毁灭的目标在其自身回合内的攻击时,攻击者伤害提升
        // TODO 自身回合内
        return character == from || character.isHaveState(StateHuiMie.privateName);
    }

    @Override
    public void doInfluence(AttackType attackType, Info info) {
        // 提示5%,lv5-提升增至10%
        info.getTraceableNumber().mul(level == 5 ? 1.1 : 1.05, privateName);
    }
}
