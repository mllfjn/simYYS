package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Info;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamage;

public class StatusChenLun extends Status implements CrowdControl, InfluenceDamage, Displayable {
    private static final String text = "沉沦";
    private final int level;
    public StatusChenLun(NaMei from, Character belongTo, int level) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        this.level = level;
        setDurationType(StatusDurationType.CHI_XU, 1);
    }

    @Override
    public boolean effective(AttackType attackType, Character character) {
        // 受到来自伊邪那美与处于毁灭的目标在其自身回合内的攻击时,攻击者伤害提升
        // TODO 自身回合内
        return character == from || character.isHaveStatus(StatusHuiMie.class);
    }

    @Override
    public void doInfluence(AttackType attackType, Info info) {
        // 提示5%,lv5-提升增至10%
        info.getTraceableNumber().mul(level == 5 ? 1.1 : 1.05, text);
    }

    @Override
    public String getText() {
        return text;
    }
}
