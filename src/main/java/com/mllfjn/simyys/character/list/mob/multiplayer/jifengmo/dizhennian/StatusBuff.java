package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusBuff extends Status implements Displayable, AttributeModifier {
    private final BuffType buffType;

    public StatusBuff(Character from, Character belongTo, BuffType buffType, int duration) {
        super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
        this.buffType = buffType;

        if (duration > 0) {
            setDurationType(StatusDurationType.CHI_XU, duration);
        }
    }

    @Override
    public String getDisplayText() {
        StringBuilder sb = new StringBuilder(buffType.desc);
        int duration = getDuration();
        if (duration != 0) {
            sb.append(duration);
        }
        return sb.toString();
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == buffType.attribute;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        double rtValue = buffType.attribute == Attribute.ATTACK ? belongTo.getInitBaseAttack() : 100;
        if (getDuration() == 0) {
            rtValue /= 2;
        }
        return rtValue;
    }

    enum BuffType {
        ATTACK("攻击", Attribute.ATTACK),
        CRIT_RATE("暴击", Attribute.CRIT_RATE),
        CRIT_POWER("爆伤", Attribute.CRIT_POWER),
        SPEED("速度", Attribute.SPEED);

        public final String desc;
        public final Attribute attribute;

        BuffType(String desc, Attribute attribute) {
            this.desc = desc;
            this.attribute = attribute;
        }
    }
}
