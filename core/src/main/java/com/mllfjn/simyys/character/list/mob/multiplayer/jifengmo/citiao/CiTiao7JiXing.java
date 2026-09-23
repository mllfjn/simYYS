package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.utils.Utils;

import java.util.List;

public class CiTiao7JiXing {
    public static final String CiTiaoName = "疾行";

    public static void install(Character character) {
        character.bp.addPriorityMove(character, () -> {
            List<Character> targets = new CharacterFinder(character)
                    .filterEnemy()
                    .getList();

            for (Character target : targets) {
                Status.of(CiTiaoName + "-行动后拉条", character, target)
                        .runOn(Trigger.WHEN_ATTACK, triggerParam -> {
                            // 造成伤害时，每有1个增益状态提升4%
                            ((ParamAttackInfo) triggerParam).getAttackInfo().getTraceableNumber()
                                    .mul(1.2, CiTiaoName);
                        })
                        .runOn(Trigger.AFTER_ACTION, _ ->
                                target.doInteractive(interactive ->
                                        interactive.increaseLocation(target, 20)
                                )
                        )
                        .addTo();
            }

            Utils.information("疾行机制太乱，鉴于当前有太多增益了，改为默认满增伤，必定拉条\n" +
                    "如果有不该拉条的地方，使用设置相对位置\"-20\"来更正");
        });
    }
}
