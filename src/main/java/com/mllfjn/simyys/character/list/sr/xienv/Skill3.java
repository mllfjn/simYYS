package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.Optional;

// 造成攻击50%伤害
// 并使目标陷入蝎毒
// 若目标已陷入蝎毒,则引爆蝎毒,造成攻击75%间接伤害
// 最多使1个敌方获得蝎毒
// lv2-引爆蝎毒造成的间接伤害增加至80%
// lv3-引爆蝎毒造成的间接伤害增加至85%
// lv4-引爆蝎毒造成的间接伤害增加至90%
// lv5-陷入蝎毒的目标回合结束后,引爆蝎毒,对其造成攻击90%间接伤害
// 蝎毒:减益,印记:每层减低80点防御,上限5层.引爆后加深1层.
//      单次受到蝎女攻击100%的治疗效果后减少1层
class Skill3 extends Skill {
    public static final String SkillName = "百蝎之毒";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        XieNv belongTo = ((XieNv) getBelongTo());

        Character target = new CharacterFinder(belongTo)
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);

        Character xieDuCarrier = belongTo.getXieDuCarrier();

        // 如果携带者和目标是同一个,则引爆蝎毒
        if (xieDuCarrier == target) {

        }

        // 如果没有人携带蝎毒,则给target上蝎毒并且设置蝎毒carrier

        // 如果携带者和目标不是同一个,单纯攻击一次

        return Optional.of(target);
    }

    static class StatusXieDu extends Status implements Displayable, AttributeModifier, StatusRunnable {
        public static final String StatusName = "蝎毒";

        private int stack;

        public StatusXieDu(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return -stack * 80;
        }

        @Override
        public String getDisplayText() {
            return StatusName + stack;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND_FIRST;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            return false;
        }
    }
}
