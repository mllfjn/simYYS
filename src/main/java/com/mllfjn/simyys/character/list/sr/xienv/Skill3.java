package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill3 extends Skill {
    public static final String SkillName = "百蝎之毒";
    private static final int[] multiplier = new int[]{0, 75, 80, 85, 90, 90};

    private final Skill2 skill2;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 2, 0, 3);
        this.skill2 = skill2;
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t造成攻击50%伤害
                √\t并使目标陷入蝎毒
                √\t若目标已陷入蝎毒,则引爆蝎毒,造成攻击75%间接伤害
                √\t最多使1个敌方获得蝎毒
                √\tlv2-引爆蝎毒造成的间接伤害增加至80%
                √\tlv3-引爆蝎毒造成的间接伤害增加至85%
                √\tlv4-引爆蝎毒造成的间接伤害增加至90%
                √\tlv5-陷入蝎毒的目标回合结束后,引爆蝎毒,对其造成攻击90%间接伤害
                √\t蝎毒:减益,印记:每层减低80点防御,上限5层.引爆后加深1层.
                 \t\t单次受到蝎女攻击100%的治疗效果后减少1层
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        XieNv belongTo = ((XieNv) getBelongTo());
        Interactive interactive = belongTo.getInteractive();
        int level = getLevel();

        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);

        StatusXieDu xieDu = belongTo.getXieDu();

        interactive.attackTypical(this, target, 50, AttackType.DAN_TI);

        // 如果没有人携带蝎毒,则给target上蝎毒
        if (xieDu == null) {
            xieDu = new StatusXieDu(belongTo, target, multiplier[level], level == 5, skill2);
            target.addStatus(xieDu);
            belongTo.setStatus(xieDu);
            // 如果携带者和目标是同一个,则引爆蝎毒
        } else if (xieDu.belongTo == target) {
            xieDu.setOff();
        }

        return Optional.of(target);
    }

}
