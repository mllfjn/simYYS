package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "炎崖";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 2);
        if (level >= 5) {
            belongTo.getBp().addPriorityMove(belongTo, this::useWithoutCost);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        int level = getLevel();

        belongTo.addStatusOrChange(
                StatusYSHJ.class,
                status -> status.setDuration(3),
                () -> new StatusYSHJ(belongTo, level >= 2 ? 25 : 15, level >= 3)
        );

        if (level >= 4) {
            List<Character> list = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .getList();
            for (Character c : list) {
                StatusZhiKao.addStack(belongTo, c, 3, Skill2.this);
            }
        }

        return Optional.empty();
    }

    private class StatusYSHJ extends Status implements Displayable {

        private static final String StatusName = "御神火界";

        private final int zengShang;
        private final boolean reduceCost;

        private StatusYSHJ(Character character, int zengShang, boolean reduceCost) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.zengShang = zengShang;
            this.reduceCost = reduceCost;

            setDurationType(StatusDurationType.WEI_CHI, 3);
            character.getBp().addStatusAdder(c -> new StatusAfterActionZhiKao(character, c));
            character.getBp().addStatusAdder(c ->
                    c.team == character.team
                            ? new StatusYSHJListener(character, c)
                            : null
            );
        }

        @Override
        public String getDisplayText() {
            return StatusName + getDuration();
        }

        // 在所有目标身上，行动后炙烤
        private class StatusAfterActionZhiKao extends Status implements StatusRunnable {
            public StatusAfterActionZhiKao(Character from, Character belongTo) {
                super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return trigger == Trigger.AFTER_ACTION;
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                StatusZhiKao.addStack(from, belongTo, 1, Skill2.this);
                return false;
            }
        }

        // 在队友身上,增伤减火
        private class StatusYSHJListener extends Status implements StatusRunnable, ForceChangeCost {
            public StatusYSHJListener(Character from, Character belongTo) {
                super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return trigger == Trigger.WILL_USE_SKILL;
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                belongTo.replaceStatus(new StatusZengShang(from, belongTo));
                return false;
            }

            @Override
            public int getChange() {
                if (StatusYSHJ.this.reduceCost && CharacterFactory.isFireCharacter(belongTo)) {
                    return -1;
                } else {
                    return 0;
                }
            }

            private class StatusZengShang extends Status implements AttributeModifier {
                public StatusZengShang(Character from, Character belongTo) {
                    super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
                    setDurationType(StatusDurationType.CHI_XU, 1);
                }

                @Override
                public boolean isAffectAttribute(Attribute attribute) {
                    return attribute == Attribute.ZENG_SHANG;
                }

                @Override
                public double getInfluence(Attribute attribute, StatusModifyParam param) {
                    return StatusYSHJ.this.zengShang;
                }
            }
        }
    }

    private static class StatusZhiKao extends Status implements Displayable, StatusRunnable {
        private static final String StatusName = "炙烤";

        private final Skill2 skill2;

        private int stack;
        private boolean reduceAfterSkill;

        private StatusZhiKao(Character from, Character belongTo, int initStack, Skill2 skill2) {
            super(from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
            this.skill2 = skill2;
            stack = initStack;
        }

        static void addStack(Character from, Character belongTo, int addStack, Skill2 skill2) {
            belongTo.addStatusOrChange(
                    StatusZhiKao.class,
                    status -> status.addStack(addStack),
                    () -> new StatusZhiKao(from, belongTo, addStack, skill2)
            );
        }

        private void addStack(int addStack) {
            stack = Math.min(5, stack + addStack);
        }

        @Override
        public String getDisplayText() {
            return StatusName + stack;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEING_ATTACKED;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (CharacterFactory.isFireCharacter(attackInfo.getAttacker())) {
                attackInfo.getTraceableNumber().mul(1.16, StatusName);
                if (!reduceAfterSkill) {
                    reduceAfterSkill = true;
                    attackInfo.getSkill().addSkillEndListener(() -> {
                        stack--;
                        from.doInteractive(interactive -> {
                            AttackInfo info = new AttackInfo(from, skill2, belongTo, AttackType.DAN_TI,
                                    belongTo.getDefence()
                            );
                            info.setMultiplier(300);
                            interactive.attack(info);
                        });
                        reduceAfterSkill = false;
                        if (stack == 0) {
                            delete();
                        }
                    });
                }
            }
            return false;
        }
    }
}

