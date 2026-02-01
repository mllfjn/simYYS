package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.List;

// √     阵亡时，提升全体队友60%的伤害和20点速度，持续1回合
// √     lv2-额外提升30点速度
// √     lv3-伤害提升增至80%
// √     lv4-速度提升增至40点
// √     lv5-持续回合数+1
//      术印:增加的伤害额外提升20%,增加的速度额外提升10点 (注:该效果目前无效)

class Skill7 extends PassiveSkill {
    public static final String SkillName = "悲声哀嚎";
    private static final int[] baseZengShang = new int[]{0, 60, 60, 80, 80, 80};
    private static final int[] baseSpeed = new int[]{0, 20, 30, 30, 40, 40};

    private final int zengShang;
    private final int speed;

    public Skill7(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 7);
//        this.zengShang = baseZengShang[level] + 20 * shuYin;
        this.zengShang = baseZengShang[level];
        this.speed = baseSpeed[level];

        belongTo.addStatus(new StatusWhenDie(belongTo));
    }

    private void use() {
        Character belongTo = getBelongTo();
        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();

        for (Character character : list) {
            character.addStatus(new StatusAiHao(belongTo, character, zengShang, speed, getLevel() >= 5 ? 2 : 1));
        }

        log(null);
    }

    @Override
    public void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    public String getName() {
        return SkillName;
    }

    class StatusWhenDie extends Status implements StatusRunnable {

        public StatusWhenDie(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.DIE;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            Skill7.this.use();
            return false;
        }
    }

    static class StatusAiHao extends Status implements AttributeModifier, Displayable {

        private final int zengShang;
        private final int speed;

        public StatusAiHao(Character from, Character belongTo, int zengShang, int speed, int duration) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            this.zengShang = zengShang;
            this.speed = speed;

            setDurationType(StatusDurationType.CHI_XU, duration);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ZENG_SHANG || attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            if (attribute == Attribute.ZENG_SHANG) {
                return zengShang;
            }

            return speed;
        }

        @Override
        public String getDisplayText() {
            return SkillName + getDuration();
        }
    }
}