package com.mllfjn.simyys.character.status;

import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class Status implements Serializable {
//    public String name;
    public final Character from;
    public final Character belongTo;
    public final StatusType statusType;
    public final StatusForm statusForm;
    private StatusDurationType durationType = StatusDurationType.NONE;
    private int duration = 1;

    public Status(Character from, Character belongTo, StatusType statusType, StatusForm statusForm) {
        this.from = from;
        this.belongTo = belongTo;
        this.statusType = statusType;
        this.statusForm = statusForm;
    }

    public void setSettleType(StatusDurationType settleType, int duration) {
        if (settleType == StatusDurationType.WEI_CHI) {
            from.removeMaintainedStatus(this);
        }

        this.durationType = settleType;
        this.duration = duration;

        if (settleType == StatusDurationType.WEI_CHI) {
            from.addMaintainedStatus(this);
        }
    }

    public StatusDurationType getDurationType() {
        return durationType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int num) {
        if (durationType == StatusDurationType.NONE) {
            throw new RuntimeException("给无持续判定的状态设置持续回合");
        }
        duration = num;
    }

    public final void delete() {
        beforeDelete();
        belongTo.getStatuses().remove(this);

        // 这里不能直接在维持列表中删除,因为可能会从维持列表中遍历并调用delete方法,如果后续有需要维持状态主动删除,需要修改
    }

    public void beforeDelete() {}
}
