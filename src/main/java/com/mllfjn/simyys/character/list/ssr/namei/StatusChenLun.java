package com.mllfjn.simyys.character.list.ssr.namei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;

public class StatusChenLun extends Status implements CrowdControl, InfluenceDamageBeingAttack, Displayable {
    private static final String text = "沉沦";
    private final int level;
    public StatusChenLun(NaMei from, Character belongTo, int level) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        this.level = level;
        setDurationType(StatusDurationType.CHI_XU, 1);
    }

    @Override
    public void doInfluenceBeingAttack(AttackType attackType, InteractiveInfo interactiveInfo) {
        // 受到来自伊邪那美与处于毁灭的目标在其自身回合内的攻击时,攻击者伤害提升
        Character attacker = interactiveInfo.getAttacker();
        if ((attacker == from || attacker.isHaveStatus(StatusHuiMie.class)) && attacker.isInRound()) {
            // 提升5%,lv5-提升增至10%
            interactiveInfo.getTraceableNumber().mul(level >= 5 ? 1.1 : 1.05, text);
        }
    }

    @Override
    public String getText() {
        return text;
    }
}
