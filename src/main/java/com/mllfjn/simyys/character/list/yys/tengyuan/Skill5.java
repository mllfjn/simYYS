package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

import java.util.List;
import java.util.Optional;

class Skill5 extends Skill {
    static final String SkillName = "琴月齐鸣";
    private static final int[] increases = new int[]{0, 10, 13, 16, 20, 20};

    private final int shuYin;

    public Skill5(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 5);
        this.shuYin = shuYin;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return ((TengYuanDaoZhang) getBelongTo()).getLvYin().canUse(1) && super.canUse(bp);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t消耗所有律音,每消耗1层提升友方全体10%攻击(至多叠加10层),持续1回合
                √\t当消耗的律音不少于6层时,解除友方全体控制效果
                √\tlv2-攻击提升增至13%
                √\tlv3-攻击提升增至16%
                √\tlv4-攻击提升增至20%
                √\tlv5-持续回合增至2
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        TengYuanDaoZhang belongTo = (TengYuanDaoZhang) getBelongTo();
        StatusLvYin lvYin = belongTo.getLvYin();
        // 消耗的层数
        int count = lvYin.getStack();
        lvYin.use(count);
        // 是否可以解控:消耗律音不少于6层
        boolean removeCrowdControl = (count >= 6);
        // 持续回合数:lv5-2回合,否则1回合
        int duration = (getLevel() >= 5) ? 2 : 1;
        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();

        for (Character character : list) {
            character.addStatus(new StatusAttack(belongTo, character,
                    (double) (count * increases[getLevel()] + shuYin) / 100, duration));
            if (removeCrowdControl) {
                character.removeAllCrowControl();
            }
        }

        return Optional.empty();
    }

    static class StatusAttack extends Status {
        private final static String StatusName = "琴月";

        public StatusAttack(Character from, Character belongTo, double ratio, int duration) {
            super(StatusName, from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);

            duration(StatusDurationType.CHI_XU, duration);
            displayNameAndDuration();
            attribute(Attribute.ATTACK, _ -> belongTo.getInitBaseAttack() * ratio);
        }
    }
}
