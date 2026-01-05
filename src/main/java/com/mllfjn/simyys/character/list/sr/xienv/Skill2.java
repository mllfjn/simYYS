package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;

// 引爆蝎毒造成伤害时,自身恢复伤害20%的生命
// 敌方每有1层蝎毒,提升自身20%攻击
// 引爆蝎毒恢复生命溢出时,将溢出恢复量的100%平均分配给其他友方
// lv2-蝎毒使目标减疗40%
// lv3-引爆蝎毒恢复比例提升至30%
// lv4-蝎毒超过1层时,每层额外使目标减疗10%
// lv5-陷入5层蝎毒的敌方目标受到间接伤害回合后,向其他敌方溅射该伤害60%的间接伤害(此伤害不超过蝎女攻击上限800%)

class Skill2 extends PassiveSkill {
    public static final String SkillName = "以毒攻毒";

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
