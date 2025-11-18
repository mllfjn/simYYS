package com.mllfjn.simyys.state;

import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class State implements Serializable {
//    public String name;
    public final Character from;
    public final Character belongTo;
    public final StateType stateType;
    public final StateForm stateForm;
    private StateSettleType settleType = StateSettleType.NONE;
    private int duration = 1;

    public State(Character from, Character belongTo, StateType stateType, StateForm stateForm) {
        this.from = from;
        this.belongTo = belongTo;
        this.stateType = stateType;
        this.stateForm = stateForm;
    }

    public void setSettleType(StateSettleType settleType, int duration) {
        if (settleType == StateSettleType.WEI_CHI) {
            from.removeMaintainedState(this);
        }

        this.settleType = settleType;
        this.duration = duration;

        if (settleType == StateSettleType.WEI_CHI) {
            from.addMaintainedState(this);
        }
    }

    public StateSettleType getSettleType() {
        return settleType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int num) {
        duration = num;
    }

    public void delete() {
        beforeDelete();
        belongTo.getStates().remove(this);

        // 这里不能直接在维持列表中删除,因为可能会从维持列表中遍历并调用delete方法,如果后续有需要维持状态主动删除,需要修改
        /*if (settleType == StateSettleType.WEI_CHI) {
            from.removeMaintainedState(this);
        }*/
    }

    public void beforeDelete() {}
}
