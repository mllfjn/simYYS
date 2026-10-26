package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunAfterCauseAttack;
import com.mllfjn.simyys.interactive.AttackInfo;

public class FuYi extends Equip implements YuHunAfterCauseAttack {
    public static final String YuHunName = "蝠翼";
    private static final Skill skill = Skill.getInstance(YuHunName);

    @Override
    public void action(AttackInfo attackInfo) {
        character.doInteractive(skill, interactive ->
                interactive.recovery(character, 0.2 * attackInfo.getTraceableNumber().getNumber())
        );
        yuHunEffect();
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
    }

    @Override
    public String getName() {
        return YuHunName;
    }
}
