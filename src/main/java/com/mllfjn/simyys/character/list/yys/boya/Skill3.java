package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;
import java.util.Optional;

// √     增加全体友方15%的暴击伤害，持续2回合
// √     拥有该增益的队友进行攻击时,黑豹有20%的概率对一个随机敌方释放[skill8]
// √     lv2-暴击伤害增加提升至20%
// √     lv3-秘术·穷追[skill8]的触发概率增加为50%
// √     lv4-暴击伤害增加提升至30%
// √     lv5-影分身存在时,额外提升友方全体15%行动条
// √     术印:秘术·豹眼黑豹释放秘术·穷追[skill8]的概率额外提升8%；额外提升全体友方10%普攻伤害

class Skill3 extends Skill {
    public static final String SkillName = "秘术·豹眼";

    private final int critPower;
    private final int rate;
    private final double increase;

    public Skill3(Character character, int level, int shuYin) {
        super(character, level, 0, 0, 3);
        this.critPower = level >= 4 ? 30 : level >= 2 ? 20 : 15;
        this.rate = (level >= 3 ? 50 : 20) + shuYin * 8;
        this.increase = 1 + level * 0.1;
    }

    private void doInfluence(InteractiveInfo interactiveInfo) {
        if (increase == 1) {
            return;
        }
        Skill fromSkill = interactiveInfo.getSkill();
        if (fromSkill instanceof Skill1PuGongBase) {
            interactiveInfo.getTraceableNumber().mul(increase, SkillName);
        }
    }

    private void doWhenAttack(ParamUseSkill pus) {
        BoYa boYa = (BoYa) getBelongTo();
        if (RateController.otherWhether(SkillName, "协同攻击", boYa.bp.calc, rate)) {
            Character target = new CharacterFinder(boYa)
                    .filterEnemy()
                    .getRandom();
            boYa.getSkill8().ifPresent(skill8 -> skill8.use(target));
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        BoYa boYa = (BoYa) getBelongTo();
        Interactive interactive = boYa.getInteractive();

        List<Character> list = new CharacterFinder(boYa)
                .filterTeammate()
                .getList();

        boolean increase = getLevel() >= 5 && boYa.getYinFenShen().isPresent();

        for (Character character : list) {
            StatusBaoYan.install(boYa, character, this);
            if (increase) {
                interactive.increaseLocation(character, 15);
            }
        }

        return Optional.empty();
    }

    static class StatusBaoYan extends Status implements Displayable, AttributeModifier
            , InfluenceDamageWhenAttack, StatusRunnable {
        public static final String StatusName = "豹眼";

        private final Skill3 skill;

        public StatusBaoYan(Character from, Character belongTo, Skill3 skill) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            this.skill = skill;

            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        public static void install(Character from, Character belongTo, Skill3 skill) {
            belongTo.getStatus(StatusBaoYan.class).ifPresentOrElse(
                    status -> status.setDuration(2),
                    () -> belongTo.addStatus(new StatusBaoYan(from, belongTo, skill))
            );
        }

        @Override
        public String getDisplayText() {
            return StatusName + getDuration();
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.CRIT_POWER;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return skill.critPower;
        }

        @Override
        public void doInfluenceWhenAttack(AttackType attackType, InteractiveInfo interactiveInfo) {
            skill.doInfluence(interactiveInfo);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.USED_SKILL || trigger == Trigger.USE_PU_GONG;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamUseSkill pus) {
                skill.doWhenAttack(pus);
            }
            return false;
        }
    }
}
