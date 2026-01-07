package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

// 创造除一个继承自身90%的属性且不可被攻击的影分身
// 源博雅每攻击3次,影分身将对同一目标释放源博雅上次攻击相同的技能
// lv2-释放后提升自身30%暴击
// lv3-继承的属性提升至100%
// lv4-释放后提升自身30点速度
// lv5-释放后提升自身70%行动条
class Skill4 extends Skill {
    public static final String SkillName = "秘术·影分身";

    public Skill4(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 4);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }
}
