package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill2 {
    public static final String SkillName = "洪福降临";
    public Skill2(Character belongTo) {
        // 自身回合结束时，击退全体敌方目标10%行动条
        // lv2-击退行动条效果提升至15%

        // 若回合中释放过委以重任,则开始打盹
        // lv4-打盹额外获得1点鬼火

        // lv3-回合结束时额外获得2点鬼火

        // lv5-战斗开始后,自身首次受到伤害时开始打盹
    }
}
