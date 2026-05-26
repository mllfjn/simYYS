package com.mllfjn.simyys.interactive;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

public class HealInfo extends InteractiveInfo {

    public HealInfo(Character attacker, Skill skill, Character target, double basicNumber) {
        super(attacker, skill, target, basicNumber);
    }

    // 基本治疗
    public static HealInfo createTypicalHeal(Character owner, Skill skill, Character target, int multiplier) {
        HealInfo interactiveInfo = new HealInfo(owner, skill, target, owner.getMaxHp());
        interactiveInfo.multiplier = multiplier;
        return interactiveInfo;
    }

    public static HealInfo createHeal(Character owner, Skill skill, Character target, double basicNumber) {
        return new HealInfo(owner, skill, target, basicNumber);
    }

    // 恢复,不会暴击
    public static HealInfo createRecovery(Character attacker, Skill skill, Character target, double basicNumber) {
        HealInfo interactiveInfo = new HealInfo(attacker, skill, target, basicNumber);
        interactiveInfo.canCrit = false;
        return interactiveInfo;
    }
}
