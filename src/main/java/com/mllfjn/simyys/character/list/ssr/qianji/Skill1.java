package com.mllfjn.simyys.character.list.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

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
    public Optional<Character> usePrivate(BattlePane bp) {
        Character target = new CharacterFinder(getBelongTo())
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getPriorAuto(CharacterFinder.Property.HP, CharacterFinder.Criteria.MIN);
        Interactive interactive = getBelongTo().getInteractive();

        // 对敌方目标造成攻击(系数)伤害
        interactive.attack(this, target, multiplier[getLevel()], AttackType.DAN_TI);

        // lv5-若海原贝戟存在,增加1层潮声
        if (getLevel() == 5) {
            HaiYuanBeiJi haiYuanBeiJi = ((QianJi) getBelongTo()).getHaiYuanBeiJi();
            if (haiYuanBeiJi != null) {
                haiYuanBeiJi.addChaoSheng(1);
            }
        }
        return Optional.of(target);
    }
}
