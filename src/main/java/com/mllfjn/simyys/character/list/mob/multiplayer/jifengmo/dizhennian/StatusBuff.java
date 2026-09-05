package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusBuff extends Status {
    public StatusBuff(Character from, Character belongTo, BuffType buffType, int duration) {
        super("地震鲶" + buffType.desc, from, belongTo);
        type(StatusType.BUFF, StatusForm.ZHUANG_TAI);

        // 用duration = 0 表示海坊主身上无时间限制的状态
        if (duration > 0) {
            duration(StatusDurationType.CHI_XU, duration);
        }
        display(() -> {
            StringBuilder sb = new StringBuilder(getName());
            if (duration != 0) {
                sb.append(getDuration());
            }
            return sb.toString();
        });
        attribute(buffType.attribute, _ -> {
            double rtValue = buffType.attribute == Attribute.ATTACK ? belongTo.getInitBaseAttack() : 100;
            if (getDuration() == 0) {
                rtValue /= 2;
            }
            return rtValue;
        });
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
