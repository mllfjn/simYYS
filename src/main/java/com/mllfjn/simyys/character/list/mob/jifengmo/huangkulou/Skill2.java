package com.mllfjn.simyys.character.list.mob.jifengmo.huangkulou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceNumberBeingHeal;
import com.mllfjn.simyys.character.status.instance.StatusConfusion;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.interactive.InteractiveInfo;

import java.util.List;
import java.util.Optional;

class Skill2 extends Skill {
    public static final String SkillName = "毒雾冲击";

    public Skill2(Character belongTo) {
        super(belongTo, 0, 0, 4, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();

        List<Character> targets = new CharacterFinder(belongTo)
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getList();

        // 对全体敌方单位分别造成2段伤害(实际是3段，可能是毒雾的)
        interactive.attackTypical(this, targets, 100, AttackType.QUN_TI);
        interactive.attackTypical(this, targets, 50, AttackType.QUN_TI);
        interactive.attackTypical(this, targets, 100, AttackType.QUN_TI);

        // 随后喷射毒雾，对全体敌方造成高额伤害，同时有100%概率附加3回合的毒伤、禁疗、抵抗降低、攻击降低和速度降低效果
        // 这里的五种效果是可以分别抵抗的

        interactive.effect(this, StatusDuShang.StatusName, targets, 100, true, StatusDuShang::new);
        interactive.effect(this, StatusJinLiao.StatusName, targets, 100, true, StatusJinLiao::new);
        interactive.effect(this, StatusResist.StatusName, targets, 100, true, StatusResist::new);
        interactive.effect(this, StatusAttack.StatusName, targets, 100, true, StatusAttack::new);
        interactive.effect(this, StatusSpeed.StatusName, targets, 100, true, StatusSpeed::new);

        // 25%概率附加1回合的混乱效果
        interactive.effect(this, StatusConfusion.StatusName, targets, 25, true
                , (from, to) -> new StatusConfusion(from, to, 1));

        return Optional.empty();
    }

    static class StatusDuShang extends Status implements StatusRunnable, Displayable {
        public static final String StatusName = "毒伤";

        public StatusDuShang(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 3);
        }

        @Override
        public String getText() {
            return StatusName + getDuration();
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            // 毒伤每回合造成攻击力33%的伤害。
            from.doInteractive(interactive ->
                    interactive.attackTypical(Skill.getInstance(StatusName), belongTo, 33, AttackType.DAN_TI));
            return false;
        }
    }

    static class StatusJinLiao extends Status implements Displayable, InfluenceNumberBeingHeal {
        public static final String StatusName = "禁疗";

        public StatusJinLiao(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 3);
        }

        @Override
        public String getText() {
            return StatusName + getDuration();
        }

        @Override
        public void doInfluenceBeingHeal(InteractiveInfo interactiveNumberInfo) {
            interactiveNumberInfo.getTraceableNumber().set(1, StatusName);
        }
    }

    static class StatusResist extends Status implements Displayable, AttributeModifier {
        public static final String StatusName = "减抗";

        public StatusResist(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 3);
        }

        @Override
        public String getText() {
            return StatusName + getDuration();
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.EFFECT_RESIST_RATE;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            // 不知道降多少随便填的
            return -30;
        }
    }

    static class StatusAttack extends Status implements Displayable, AttributeModifier {
        public static final String StatusName = "减攻";

        public StatusAttack(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 3);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            // 不知道降多少随便填的
            return belongTo.getInitAttack() * -0.3;
        }

        @Override
        public String getText() {
            return StatusName;
        }
    }

    static class StatusSpeed extends Status implements Displayable, AttributeModifier {
        public static final String StatusName = "减速";

        public StatusSpeed(Character from, Character belongTo) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 3);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            // 不知道降多少随便填的
            return belongTo.getInitSpeed() * -0.3;
        }

        @Override
        public String getText() {
            return StatusName;
        }
    }
}
