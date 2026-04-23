package com.mllfjn.simyys.character.list.sp.kongmian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.*;

import java.util.List;
import java.util.Optional;

//      一线目附身敌方目标,若敌方被一线目附身,先引燃再重新附身
//      并为全体友方增加25%行动条
//      lv2-回合开始时,将等同于自身攻击131%的怨痕印刻于一线目中
//      lv3-将等同于友方造成伤害的20%的怨痕印刻于一线目中
//      lv4-受到伤害时,将此伤害的30%印刻于一线目中并减免
//      lv5-一线目重复附身同一目标时,使其被动失效直到空相面灵气下一回合结束,且不可被驱散


class Skill3 extends Skill {
    private static final String SkillName = "梦虚空境";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
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
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);

        // 一线目附身敌方目标,若敌方被一线目附身，先引燃再重新附身
        target.getStatus(StatusYiXianMu.class).ifPresent(Status::delete);
        double max = ((Skill2) belongTo.getSkill(2).orElseThrow()).getMax();
        StatusYiXianMu statusYiXianMu = new StatusYiXianMu(belongTo, target, getLevel(), max);
        target.addStatus(statusYiXianMu);


        // 并为全体友方增加25%行动条
        List<Character> targets = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();
        for (Character character : targets) {
            interactive.increaseLocation(character, 25);
        }

        return Optional.of(target);
    }

    static class StatusYiXianMu extends Status implements AttributeModifier, Displayable {
        private static final String StatusName = "一线目";

        private static final Skill skill = Skill.getInstance(StatusName);

        private final double max;
        private double yuanHen = 0;
        private final StatusYXMBeingAttackListener statusYXMBeingAttackListener;
        private final StatusYXMBeforeRoundListener statusYXMBeforeRoundListener;
        private final StatusYXMReduceDamageListener statusYXMReduceDamageListener;

        public StatusYiXianMu(Character from, Character belongTo, int level, double max) {
            super(from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);

            // lv2-回合开始时,将等同于自身攻击131%的怨痕印刻于一线目中
            if (level >= 2) {
                statusYXMBeforeRoundListener = new StatusYXMBeforeRoundListener(from, this);
                from.addStatus(statusYXMBeforeRoundListener);
            } else {
                statusYXMBeforeRoundListener = null;
            }

            // lv3-将等同于友方(这里是对面的人)造成伤害的20%的怨痕印刻于一线目中
            if (level >= 3) {
                statusYXMBeingAttackListener = new StatusYXMBeingAttackListener(belongTo, this);
                belongTo.addStatus(statusYXMBeingAttackListener);
            } else {
                statusYXMBeingAttackListener = null;
            }
            this.max = max;

            // lv4-受到伤害时,将此伤害的30%印刻于一线目中并减免
            if (level >= 4) {
                statusYXMReduceDamageListener = new StatusYXMReduceDamageListener(belongTo, this);
                from.addStatus(statusYXMReduceDamageListener);
            } else {
                statusYXMReduceDamageListener = null;
            }

            // 持续3个回合后自动引燃
            setDurationType(StatusDurationType.CHI_XU, 3);
        }

        public void yinRan() {
            // 引燃后移除,造成等同一线目种怨痕值100%的间接伤害,最多不超过友方出场式神初始总攻击的4000%
            if (yuanHen > 0) {
                AttackInfo info = AttackInfo.createJianJieAttack(from, skill
                        , belongTo, (from, to) -> yuanHen);
                info.setLimit(max);

                from.doInteractive(interactive -> {
                    interactive.attack(info);
                    skill.useDone();
                });
            }
        }

        public void add(double num) {
            yuanHen += num;
        }

        @Override
        public void beforeDelete() {
            yinRan();
            if (statusYXMBeingAttackListener != null) {
                statusYXMBeingAttackListener.delete();
            }
            if (statusYXMBeforeRoundListener != null) {
                statusYXMBeforeRoundListener.delete();
            }
            if (statusYXMReduceDamageListener != null) {
                statusYXMReduceDamageListener.delete();
            }
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            // 降低目标20%防御
            return -belongTo.getInitDefense() * 0.2;
        }

        @Override
        public String getDisplayText() {
            return StatusName + getDuration();
        }

        static class StatusYXMBeingAttackListener extends Status implements StatusRunnable {
            private final StatusYiXianMu statusYXM;

            public StatusYXMBeingAttackListener(Character character, StatusYiXianMu statusYXM) {
                super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
                this.statusYXM = statusYXM;
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return trigger == Trigger.AFTER_ATTACK;
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                if (param instanceof ParamAttackInfo pa) {
                    InteractiveInfo info = pa.attackInfo;
                    if (info.getAttacker().team != belongTo.team) {
                        statusYXM.add(info.getTraceableNumber().getNumber() * 0.2);
                    }
                }
                return false;
            }
        }

        static class StatusYXMBeforeRoundListener extends Status implements StatusRunnable {
            private final StatusYiXianMu statusYXM;

            public StatusYXMBeforeRoundListener(Character character, StatusYiXianMu statusYXM) {
                super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
                this.statusYXM = statusYXM;
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return trigger == Trigger.BEFORE_ROUND;
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                // 等同于自身攻击131%
                statusYXM.yuanHen += belongTo.getAttack() * 1.31;
                return false;
            }
        }

        static class StatusYXMReduceDamageListener extends Status implements InfluenceDamageBeingAttack {
            private final StatusYiXianMu statusYXM;

            public StatusYXMReduceDamageListener(Character character, StatusYiXianMu statusYXM) {
                super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
                this.statusYXM = statusYXM;
            }

            @Override
            public void doInfluenceBeingAttack(AttackInfo attackInfo) {
                // lv4-受到伤害时,将此伤害的30%印刻于一线目中并减免
                TraceableNumber traceableNumber = attackInfo.getTraceableNumber();
                double number = traceableNumber.getNumber() * 0.3;

                traceableNumber.mul(0.7, StatusYiXianMu.StatusName);
                statusYXM.add(number);
            }
        }
    }
}
