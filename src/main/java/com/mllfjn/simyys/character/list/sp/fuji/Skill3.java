package com.mllfjn.simyys.character.list.sp.fuji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "降蛊之瞳";
    private static final int[] multiplier = {0, 70, 85, 85, 100, 100};

    private final int defensePerStack;

    public Skill3(Character belongTo, int level, int defensePerStack) {
        super(belongTo, level, 3, 0, 3);
        this.defensePerStack = defensePerStack;
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t攻击目标3次,每次造成攻击70%伤害
                \t\t并附加1层瞳蛊,维持1回合
                \t蛇灵返回后,技能替换为[爱怨灼身]
                √\tlv2-每次造成攻击85%伤害
                \tlv3-瞳蛊可在目标受到行动条增加效果时触发,消耗1层使其无效
                √\tlv4-每次造成攻击100%伤害
                \tlv5-瞳蛊触发时,降低目标8%最大生命值上限(每次不超过攻击的1200%,最多降低至20%)
                \t瞳蛊:减益,印记.受到治疗效果时触发,消耗1层使其无效,上限3层
                """;
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
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        for (int i = 0; i < 3; i++) {
            interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
            StatusFJReduceDefense.addStack(belongTo, target, defensePerStack);
        }
        return Optional.of(target);
    }
}
