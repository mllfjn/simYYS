package com.mllfjn.simyys.character.list.yys.yuanlaiguang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;

import java.util.Optional;

class Skill5 extends Skill {
    static final String SkillName = "血之契";

    private final int shuYin;

    public Skill5(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 3, 5);
        this.shuYin = shuYin;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character yuan = getBelongTo();
        Character target = new CharacterFinder(yuan)
                .filterTeammate()
                .filterSelf()
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        // 提升自身lv1-22, lv2-26, lv4-30暴击
        int level = getLevel();
        Status.of(SkillName + "暴击", yuan)
                .attribute(Attribute.CRIT_RATE, level == 1 ? 22 : level < 4 ? 26 : 30)
                .addTo();

        // 术印·血契额外提升自身与目标15%暴击伤害
        if (shuYin > 0) {
            Status.of(SkillName + "爆伤", yuan, yuan)
                    .attribute(Attribute.CRIT_POWER, 15 * shuYin)
                    .addTo();
            Status.of(SkillName + "爆伤", yuan, target)
                    .attribute(Attribute.CRIT_POWER, 15 * shuYin)
                    .addTo();
        }

        // 与鬼胄相关的内容
        getBelongTo().getSkill(6).ifPresent(skill -> {
            Skill6Passive skill6 = ((Skill6Passive) skill);
            // lv3, lv5-释放后鬼兵部伤害吸收系数提升
            if (level >= 3) {
                skill6.addAbsorb(level >= 5 ? 0.2 : 0.1);
            }

            // 鬼兵部同时附身给另一友方目标
            skill6.fuShen(target);
        });


        return Optional.of(target);
    }
}
