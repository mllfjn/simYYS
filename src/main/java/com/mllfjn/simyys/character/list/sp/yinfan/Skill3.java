package com.mllfjn.simyys.character.list.sp.yinfan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

// √     对敌方全体造成攻击138%伤害
// √     邀战带有愿佑的友方目标
// √     并使下次[skill2]鬼火消耗减少1点
// √     lv2-每提供1点愿力,自身暴击伤害增加5%,最多增加120%
// √     lv3-每提供1点愿力,自身获得等量于最大生命值12%的护盾,最多叠加8层,持续2回合
//      lv4-若释放时累计提供12点愿力,无视敌方御魂和被动效果
//      lv5-若释放时累计提供24点愿力,召唤因幡兔释放[月之洗礼]
//       月之洗礼:对敌方全体再次造成攻击153%伤害,本次伤害无视敌方御魂和被动效果

class Skill3 extends Skill {
    private static final String SkillName = "寂光映月";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();

        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MIN);

        List<Character> enemy = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        // 对敌方全体造成攻击138%伤害
        interactive.attackTypical(this, enemy, 138, AttackType.QUN_TI);

        List<Character> carriers = new CharacterFinder(belongTo)
                .filterTeammate()
                .filter(character -> character.isHaveStatus(StatusYuanYou.class))
                .getList();

        // 邀战带有愿佑的友方目标
        for (Character carrier : carriers) {
            carrier.xieZhan(this, target);
        }

        // 并使下次[skill2]鬼火消耗减少1点
        belongTo.getSkill(2).ifPresent(skill2 -> ((Skill2) skill2).reduceCost());

        return Optional.of(target);
    }
}
