package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunAfterCauseAttack;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;

public class FuYi extends Equip implements YuHunAfterCauseAttack {
    public static final String YuHunName = "蝠翼";
    private static final Skill skill = Skill.getInstance(YuHunName);

    @Override
    public void action(AttackInfo attackInfo, Interactive interactive) {
        interactive.recovery(skill, character,
                0.2 * attackInfo.getTraceableNumber().getNumber()
        );
        yuHunEffect();
        skill.useDone();
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
