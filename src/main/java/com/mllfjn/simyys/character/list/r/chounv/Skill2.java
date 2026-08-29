package com.mllfjn.simyys.character.list.r.chounv;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.RetainAfterChangeWave;
import com.mllfjn.simyys.character.status.determinant.RetainAfterDie;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.StatusSupplier;

// √    行动结束时，有20%基础概率对随机(此处有错误,应该是最高生命百分比)敌人附加咒火，持续2回合
// √    lv2-咒火易伤效果增至10%
// √    lv3-基础概率增至30%
// √    lv4-咒火易伤效果增至15%
// √    lv5-基础概率增至40%
// √    咒火:5%易伤

class Skill2 extends PassiveSkill {
    private static final String SkillName = "咒火";

    private final Status status;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        status = new StatusXiaZhou(belongTo, level);
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(status);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(status);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    class StatusXiaZhou extends Status implements StatusRunnable, RetainAfterDie, RetainAfterChangeWave {
        private final int yiShang;
        private final int rate;

        public StatusXiaZhou(Character character, int level) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            rate = level >= 5 ? 40 : level >= 3 ? 30 : 20;
            yiShang = level >= 4 ? 15 : level >= 2 ? 10 : 5;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ACTION;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            // 行动结束时，有(概率)对随机敌人附加咒火，持续2回合
            Character target = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MAX);
            belongTo.doInteractive(interactive ->
                    interactive.effect(Skill2.this, target, rate, true, StatusZhouHuo.getSupplier(yiShang)));

            return false;
        }
    }

    static class StatusZhouHuo extends Status implements AttributeModifier, Displayable {
        private final double yiShang;

        public StatusZhouHuo(Character from, Character belongTo, double yiShang) {
            super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            this.yiShang = yiShang;
            setDurationType(StatusDurationType.CHI_XU, 2);
        }

        public static StatusSupplier getSupplier(double yiShang) {
            return new StatusSupplier(SkillName, StatusZhouHuo.class, (from, to) ->
                    to.getStatus(StatusZhouHuo.class).ifPresentOrElse(
                            status -> {
                                if (status.getDuration() < 2) {
                                    status.setDuration(2);
                                }
                            },
                            () -> to.addStatus(new StatusZhouHuo(from, to, yiShang))
                    )
            );
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.YI_SHANG;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return yiShang;
        }

        @Override
        public String getDisplayText() {
            return SkillName + getDuration();
        }
    }

}
