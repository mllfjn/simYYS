package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class NiePanZhiHuo extends Equip implements YuHunSealResponse {
    public static final String YuHunName = "涅槃之火";
    private static final Skill skill = Skill.getInstance("涅槃之火");

    private Status status;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = Status.of(YuHunName, character);
        status.runOn(Trigger.AFTER_ACTION, _ -> {
            if (character.getHpPercent() < 0.3) {
                character.doInteractive(interactive -> {
                    interactive.healTypical(skill, character, 15);
                    yuHunEffect();
                    skill.useDone();
                });
            }
        });
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(status);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(status);
    }
}
