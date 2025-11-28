package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill {
    public static final String SkillName = "千汐";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 120, 125};
    public Skill1(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 1);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        Character target = CharacterFinder.findPriorAuto(bp, CharacterFinder.getEnemyTeam(getBelongTo()), CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);
        lastUsedTarget = target;
        Interactive interactive = getBelongTo().getInteractive();

        // 对敌方目标造成攻击(系数)伤害
        interactive.attack(SkillName, target, multiplier[getLevel()], AttackType.DAN_TI);

        // lv5-若海原贝戟存在,增加1层潮声
        if (getLevel() == 5) {
            HaiYuanBeiJi haiYuanBeiJi = ((QianJi) getBelongTo()).getHaiYuanBeiJi();
            if (haiYuanBeiJi != null) {
                haiYuanBeiJi.addChaoSheng(1);
            }
        }
    }
}
