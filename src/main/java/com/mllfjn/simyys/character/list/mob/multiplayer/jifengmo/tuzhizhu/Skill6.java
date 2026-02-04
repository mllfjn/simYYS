package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkillCanNotSeal;

class Skill6 extends PassiveSkillCanNotSeal {
    private static final String SkillName = "召唤";

    public Skill6(Character belongTo) {
        super(belongTo, -1, 6);
    }

    @Override
    public String getSkillDesc() {
        return """
                \t部位被破坏时,召唤出一群小蜘蛛前来助战
                \t本体受到一定量伤害时(用转阶段实现),也会召唤一群小蜘蛛前来助战
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
