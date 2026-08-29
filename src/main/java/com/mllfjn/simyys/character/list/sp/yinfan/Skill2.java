package com.mllfjn.simyys.character.list.sp.yinfan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.Optional;

// √     创造存在1回合的幻境并获得3点愿力.
// √     幻境中自身效果抵抗增加80%
// √     除自身外的友方式神释放技能时,获得愿佑,维持1回合(实际是持续1回合,回合内的话持续2回合)
// √     lv2-幻境中御魂产生的鬼火优先转化为等量愿力
// √     lv3-幻境中自身回合开始时获得3点愿力
// √     lv4-幻境效果增加为2回合
// √     lv5-先机:释放[skill2] (好像是无消耗的)
// √     愿力:通用,印记:最多储存8点.当友方目标释放技能鬼火不足时,可使用等量愿力代替
// √     愿佑:增益,印记:获得因幡辉夜姬暴击伤害和防御属性的30%,最多获得不超过120%暴击伤害和200防御

class Skill2 extends Skill {
    private static final String SkillName = "愿满夜";

    private final int skill3Level;

    public Skill2(Character belongTo, int level, int skill3Level) {
        super(belongTo, level, 2, 0, 2);
        this.skill3Level = skill3Level;

        belongTo.bp.addPriorityMove(belongTo, this::useWithoutCost);
    }

    public void reduceCost() {
        setCost(1);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        int level = getLevel();

        StatusHuanJing.create(belongTo, level, skill3Level);
        StatusYuanLi.addYuanLi(belongTo, 3, level, skill3Level);

        setCost(2);

        return Optional.empty();
    }

    static class StatusHuanJing extends Status implements StatusRunnable, AttributeModifier {
        private final boolean getYuanLiBeforeRound;

        private final int skill2Level;
        private final int skill3Level;
        private final StatusAdder<?> adder;

        public StatusHuanJing(Character character, int duration, int skill2Level, int skill3Level) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.skill2Level = skill2Level;
            this.skill3Level = skill3Level;
            this.getYuanLiBeforeRound = skill2Level >= 3;
            duration(StatusDurationType.WEI_CHI, duration);

            adder = belongTo.bp.addStatusAdder(c ->
                    c.team == character.team && c != character && !c.isYYS()
                            ? new StatusUseSkillListener(character, c)
                            : null
            );
        }

        public static void create(Character character, int skill2Level, int skill3Level) {
            int duration = skill2Level >= 4 ? 2 : 1;
            character.getStatus(StatusHuanJing.class).ifPresentOrElse(
                    status -> status.duration(duration)
                    , () -> character.addStatus(new StatusHuanJing(character, duration, skill2Level, skill3Level))
            );
        }

        @Override
        public void beforeDelete() {
            adder.deleteAndRemove();
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND && getYuanLiBeforeRound;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            StatusYuanLi.addYuanLi(belongTo, 3, skill2Level, skill3Level);
            return false;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.EFFECT_RESIST_RATE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 80;
        }

        static class StatusUseSkillListener extends Status implements StatusRunnable {

            public StatusUseSkillListener(Character from, Character belongTo) {
                super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return trigger == Trigger.WILL_USE_SKILL;
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                StatusYuanYou.install(from, belongTo);
                return false;
            }
        }
    }

}
