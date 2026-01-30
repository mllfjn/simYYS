package com.mllfjn.simyys.character.list.ssr.maochuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

// 召唤别馆私汤.
// 别馆私汤在场时,猫川的普攻系数翻倍,同时使友方全体造成的普攻伤害提升20%.
// 再次召唤别馆私汤时,恢复别馆私汤100%的生命并使友方全体享受一次温泉疗愈
// lv2-造成的普攻伤害提升至35%
// lv3-自身经过别馆私汤时,视为到达行动条终点
// lv4-造成的普攻伤害提升至50%
// lv5-先机:释放本技能(无消耗)
// 别馆私汤:固定在行动条70%的位置,免疫减益和控制效果,继承猫川攻击550%的生命值和100%防御,无法行动.
//      若自身处于可行动状态,友方每使用普攻6次,泉水化猫攻击6次,每次对随机敌方目标造成攻击125%伤害,不会触发敌方全体御魂效果,随后再次召唤别馆私汤

class Skill3 extends Skill {
    public static final String SkillName = "洗筋伐髓";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 3);
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
