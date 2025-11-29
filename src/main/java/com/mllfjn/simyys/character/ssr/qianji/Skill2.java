package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill2 extends Skill {
    public static final String SkillName = "汐梦";
    private static final int[] multiplier = new int[]{0, 172, 185, 198, 211, 211};

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        // 拉条写在千姬类里了
        Interactive interactive = getBelongTo().getInteractive();
        Character target = CharacterFinder.findPriorAuto(bp, CharacterFinder.getEnemyTeam(getBelongTo()), CharacterFinder.Property.HP, CharacterFinder.Criteria.MAX);
        lastUsedTarget = target;
        // 对敌方目标造成攻击(系数)伤害
        interactive.attack(SkillName, target, multiplier[getLevel()], AttackType.DAN_TI);
        // 若海原贝戟在场,额外附加汐梦
        if (((QianJi) getBelongTo()).getHaiYuanBeiJi() != null) {
            target.addStatus(new StatusXiMeng(getBelongTo(), target));
        }
    }
}
