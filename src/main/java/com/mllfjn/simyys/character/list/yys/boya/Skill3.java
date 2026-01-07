package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

// 增加全体友方15%的暴击伤害，持续2回合
// 拥有该增益的队友进行攻击时,黑豹有20%的概率对一个随机敌方释放[skill8]
// lv2-暴击伤害增加提升至20%
// lv3-[skill8]的触发概率增加为50%
// lv4-暴击伤害增加提升至30%
// lv5-影分身存在时,额外提升友方全体15%行动条

class Skill3 extends Skill {
    public static final String SkillName = "秘术·豹眼";

    public Skill3(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 3);
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
