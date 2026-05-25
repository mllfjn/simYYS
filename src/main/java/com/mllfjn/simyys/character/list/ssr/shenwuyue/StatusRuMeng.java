package com.mllfjn.simyys.character.list.ssr.shenwuyue;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

public class StatusRuMeng extends Status implements Displayable, ConditionalReduceCost, AttributeModifier {
    private static final String StatusName = "入梦";

    private int stack = 2;

    public StatusRuMeng(ShenWuYue from, Character belongTo) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        setDurationType(StatusDurationType.WEI_CHI, 2);
        from.setRuMeng(this, true);
    }

    public void deleteAndRemoveMaintained() {
        from.removeMaintainedStatus(this);
        delete();
    }

    @Override
    public int getMaxReduce() {
        return 3;
    }

    @Override
    public void enable(int usedCount) {
        if (stack == 1) {
            deleteAndRemoveMaintained();
        } else {
            stack--;
        }
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack + "-" + getDuration();
    }

    @Override
    public void beforeDelete() {
        ((ShenWuYue) from).setRuMeng(null, false);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.JIAN_SHANG;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return 20;
    }
}
