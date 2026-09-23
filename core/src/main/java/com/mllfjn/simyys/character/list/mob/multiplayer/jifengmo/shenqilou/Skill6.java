package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;

class Skill6 extends PassiveSkill {
    private static final String SkillName = "蜃雾笼罩";

    public Skill6(Character belongTo) {
        super(belongTo, 0, 6);
        Status.of(SkillName, belongTo)
                .runOn(Trigger.AFTER_ROUND, _ -> StatusShenWuHuDun.get(belongTo))
                .addTo();
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
