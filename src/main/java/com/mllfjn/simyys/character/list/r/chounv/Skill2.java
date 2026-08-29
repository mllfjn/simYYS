package com.mllfjn.simyys.character.list.r.chounv;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
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
        int yiShang = level >= 4 ? 15 : level >= 2 ? 10 : 5;
        int rate = level >= 5 ? 40 : level >= 3 ? 30 : 20;
        status = Status.of(SkillName, belongTo);
        // 行动结束时，有(概率)对随机敌人附加咒火，持续2回合
        status.runOn(Trigger.AFTER_ACTION, _ -> {
            Character target = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MAX);
            belongTo.doInteractive(interactive ->
                    interactive.effect(Skill2.this, target, rate, true, StatusZhouHuo.getSupplier(yiShang)));

        }).addTo();
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

    static class StatusZhouHuo extends Status {
        public StatusZhouHuo(Character from, Character belongTo, double yiShang) {
            super(SkillName, from, belongTo);
            type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 2);
            attribute(Attribute.YI_SHANG, _ -> yiShang);
            displayNameAndDuration();
        }

        public static StatusSupplier getSupplier(double yiShang) {
            return new StatusSupplier(SkillName, StatusZhouHuo.class, (from, to) ->
                    to.getStatus(StatusZhouHuo.class).ifPresentOrElse(
                            status -> {
                                if (status.getDuration() < 2) {
                                    status.duration(2);
                                }
                            },
                            () -> to.addStatus(new StatusZhouHuo(from, to, yiShang))
                    )
            );
        }
    }

}
