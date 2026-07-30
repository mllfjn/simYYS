package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.List;

class Skill6 extends PassiveSkill {
    static final String SkillName = "诸律精通";

    private final int shuYin;

    public Skill6(TengYuanDaoZhang belongTo, int level, int shuYin) {
        super(belongTo, level, 6);
        this.shuYin = shuYin;
        StatusLvYin lvYin = belongTo.getLvYin();
        lvYin.setSkill6(this);
        belongTo.addStatus(new StatusSkill6(belongTo));
        if (level >= 5) {
            lvYin.Skill6setMaxStack();
        }
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t免疫行动条击退效果
                √\t自身回合开始时获得1层律音
                √\t每获得1层律音,永久提升自身5点速度与友方全体5%暴击伤害,叠加上限等同于律音层数上限
                √\tlv2-提升的速度增至8点
                √\tlv3-自身回合开始前受到行动条击退效果时,依次获得1,2,3层律音(至多3次)
                √\tlv4-提升的暴击伤害增至8%
                √\tlv5-律音上限增至10
                """;
    }

    void getLvYin(int count, int maxStack) {
        int level = getLevel();
        Character belongTo = getBelongTo();
        StatusSpeed.addStack(belongTo, level >= 2 ? 8 : 5, maxStack, count);
        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();
        for (Character character : list) {
            StatusZhuLvForTeammates.addStack(character, level >= 4 ? 8 : 5, 2 * shuYin, maxStack, count);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusSkill6 extends Status implements StatusRunnable, IgnoreActionDecrease {
        private int ignoreActionDecreaseTimes = 0;

        public StatusSkill6(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            TengYuanDaoZhang.getLvYin(belongTo).addStack(1);
            ignoreActionDecreaseTimes = 0;
            return false;
        }

        @Override
        public void takeEffectFeedBack() {
            ignoreActionDecreaseTimes++;
            if (ignoreActionDecreaseTimes < 4) {
                TengYuanDaoZhang.getLvYin(belongTo).addStack(ignoreActionDecreaseTimes);
            }
        }
    }

    static class StatusSpeed extends Status implements AttributeModifier {
        private final int speedPerStack;
        private final int maxStack;

        private int stack;

        public StatusSpeed(Character character, int speedPerStack, int maxStack) {
            super(character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            this.speedPerStack = speedPerStack;
            this.maxStack = maxStack;
        }

        public static void addStack(Character character, int speedPerStack, int maxStack, int stack) {
            character.getStatus(StatusSpeed.class)
                    .orElseGet(() -> {
                        StatusSpeed status = new StatusSpeed(character, speedPerStack, maxStack);
                        character.addStatus(status);
                        return status;
                    }).addStack(stack);
        }

        private void addStack(int addStack) {
            stack = Math.min(stack + addStack, maxStack);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return speedPerStack * stack;
        }
    }

    static class StatusZhuLvForTeammates extends Status implements AttributeModifier {
        private final int critPowerPerStack;
        private final int effectResistPerStack;

        private final int maxStack;
        private int stack;

        public StatusZhuLvForTeammates(Character character, int critPowerPerStack, int effectResistPerStack,
                                       int maxStack) {
            super(character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            this.critPowerPerStack = critPowerPerStack;
            this.effectResistPerStack = effectResistPerStack;
            this.maxStack = maxStack;
        }

        public static void addStack(Character character, int critPowerPerStack, int effectResistPerStack,
                                    int maxStack, int stack) {
            character.getStatus(StatusZhuLvForTeammates.class)
                    .orElseGet(() -> {
                        StatusZhuLvForTeammates status = new StatusZhuLvForTeammates(character,
                                critPowerPerStack, effectResistPerStack, maxStack);
                        character.addStatus(status);
                        return status;
                    }).addStack(stack);
        }

        private void addStack(int addStack) {
            stack = Math.min(stack + addStack, maxStack);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.CRIT_POWER
                    || (attribute == Attribute.EFFECT_RESIST_RATE && effectResistPerStack > 0);
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            if (attribute == Attribute.CRIT_POWER) {
                return critPowerPerStack * stack;
            } else {
                return effectResistPerStack * stack;
            }
        }
    }
}
