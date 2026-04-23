package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "渡灵";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);
    }

    @Override
    public String getSkillDesc() {
        return """
                \t葛叶释放归源术外的妖术获得1层九尾之力。其他非召唤物友方获得技能合守
                \t[释放]使指定友方目标提升40%行动条,并获得2层狐族庇护,持续1回合
                \tlv2-使友方获得3层狐族庇护
                \tlv3-友方获得狐族庇护时,提升葛叶75点速度,至多3次,持续1回合
                \tlv4-释放使自身获得1层狐族庇护,持续1回合
                \tlv5-战斗开始时,获得1层狐族庇护,持续1回合
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.of();
    }

}
