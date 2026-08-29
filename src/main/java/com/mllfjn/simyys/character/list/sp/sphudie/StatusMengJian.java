package com.mllfjn.simyys.character.list.sp.sphudie;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusShield;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.interactive.TraceableNumber;

class StatusMengJian extends StatusShield implements Displayable, AttributeModifier {
    private static final String StatusName = "梦茧";

    private final Skill2 skill2;

    private boolean isFirst = true;
    private StatusMengJian next;

    private StatusMengJian(Character from, Character belongTo, double shield, Skill2 skill2) {
        super(from, belongTo, shield);
        this.skill2 = skill2;

        duration(StatusDurationType.CHI_XU, 2);
    }

    static void install(Character from, Character belongTo, double shield, Skill2 skill2) {
        StatusMengJian statusFirst = null;
        for (Status status : belongTo.getStatuses()) {
            if (status instanceof StatusMengJian smj) {
                if (statusFirst != null) {
                    return;
                } else {
                    statusFirst = smj;
                }
            }
        }

        StatusMengJian statusMengJian = new StatusMengJian(from, belongTo, shield, skill2);
        if (statusFirst != null) {
            statusFirst.next = statusMengJian;
            statusMengJian.isFirst = false;
        }
        belongTo.addStatus(statusMengJian);
    }

    @Override
    public void beforeDelete() {
        if (next != null) {
            next.isFirst = true;
        }
    }

    @Override
    public boolean handle(InteractiveInfo interactiveInfo) {
        TraceableNumber traceableNumber = interactiveInfo.getTraceableNumber();
        double number = traceableNumber.getNumber();
        double shield = getShield();
        if (number < shield) {
            setShield(shield - number);
            traceableNumber.sub(number, StatusName);
            return false;
        } else {
            if (skill2.isImmuneOverDoseDamage() && number > shield) {
                traceableNumber.sub(number, StatusName + "免疫过量伤害");
            } else {
                traceableNumber.sub(shield, StatusName);
            }
            return true;
        }
    }

    @Override
    public String getDisplayText() {
        if (isFirst) {
            if (next != null) {
                return StatusName + 2;
            } else {
                return StatusName;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.ZENG_SHANG;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return skill2.isReinforcement() ? 20 : 10;
    }
}
