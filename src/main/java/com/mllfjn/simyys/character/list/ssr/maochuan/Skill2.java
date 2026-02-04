package com.mllfjn.simyys.character.list.ssr.maochuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;

// 友方经过别馆私汤时享受温泉疗愈:恢复猫川攻击98%的生命并由40%概率驱散1个减益状态或控制效果,优先驱散控制效果
// lv2-自身经过别馆私汤时,获得的恢复量翻倍
// lv3-驱散概率提升至60%
// lv4-驱散概率提升至80%
// lv5-驱散概率提升至100%

class Skill2 extends PassiveSkill {
    private static final String SkillName = "泉涌";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
    }

    @Override
    public void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    public String getName() {
        return SkillName;
    }
}
