package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.InteractiveInfo;

public class CiTiao5KuangFeng {
    public static final String CiTiaoName = "狂风";

    public static void install(Character character) {
        Skill skill = Skill.getInstance(CiTiaoName);
        Status.of(CiTiaoName, character)
                .runOn(Trigger.AFTER_ATTACK, triggerParam -> {
                    InteractiveInfo interactiveInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
                    Character attacker = interactiveInfo.getAttacker();
                    // 自身（指玩家，这里是对面的队伍）回合内造成伤害时，额外对其造成真实伤害，伤害量等同于该次伤害的20%
                    if (attacker.isInRound() && interactiveInfo.getSkill() != skill) {
                        character.doInteractive(interactive -> interactive
                                .attack(AttackInfo.createRealAttack(attacker, skill, character,
                                        interactiveInfo.getTraceableNumber().getNumber() * 0.2)));
                    }
                })
                .addTo();
    }
}
