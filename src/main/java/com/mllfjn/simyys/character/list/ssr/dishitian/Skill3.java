package com.mllfjn.simyys.character.list.ssr.dishitian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "刹那莲华绽放";
    private static final int[] multiplier = new int[]{0, 138, 145, 152, 159, 159};

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t攻击敌方全体,造成攻击138, 145, 152, 159伤害,并为自身和另一生命比例最低的友方恢复攻击100%的生命
                \t\t懒得测试了,这个"另一"是不是强制排除自身,目前按排除写的
                √\t回合内释放时,对被施加金莲的敌方额外造成2次伤害
                \tlv5-施加的金莲消失时,驱散或解除自身控制效果,免消耗释放1次不触发敌方全体御魂即被动,但伤害和恢复降低40%.(对怪物不生效)
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        DiShiTian belongTo = (DiShiTian) getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();
        if (belongTo.isInRound()) {
            StatusJinLian jinLian = belongTo.getJinLian();
            if (jinLian != null) {
                for (int i = 0; i < 2; i++) {
                    list.add(jinLian.belongTo);
                }
            }
        }
        interactive.attackTypical(this, list, multiplier[getLevel()], AttackType.QUN_TI);

        interactive.recovery(this, belongTo, belongTo.getAttack());
        Character character = new CharacterFinder(belongTo)
                .filterTeammate()
                // 改这个的话要改desc
                .filterSelf()
                .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
        interactive.recovery(this, character, belongTo.getAttack());

        return Optional.empty();
    }
}
