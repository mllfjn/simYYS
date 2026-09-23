package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;

import java.util.List;
import java.util.Optional;

class Skill4 extends Skill {
    static final String SkillName = "咏叹巧韵";
    private static final int[] duration = new int[]{0, 3, 4, 4, 5, 5};
    private static final int[] increases = new int[]{0, 10, 10, 20, 20, 30};

    private final double increase;

    public Skill4(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 4);
        increase = (increases[level] + shuYin * 3) / 100.0;
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t进入奏律状态,提升友方全体10%攻击,维持3回合
                √\tlv2-维持回合增至4回合
                √\tlv3-攻击提升增至20%
                √\tlv4-维持回合增至5回合
                √\tlv5-攻击提升增至30%
                √\t奏律状态:增益,印记.免疫控制效果
                \t\t与放逐
                √\t自身回合开始后额外获得1层律音且不会行动
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        int level = getLevel();
        List<Character> list = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .getList();

        getBelongTo().addStatus(new StatusZouLv(getBelongTo(), duration[level]));

        for (Character character : list) {
            character.addStatus(
                    new StatusIncreaseAttack(getBelongTo(), character, duration[level], increase));
        }

        return Optional.empty();
    }

    static class StatusZouLv extends Status {
        private static final String StatusName = "奏律状态";

        public StatusZouLv(Character character, int duration) {
            super(StatusName, character, character, StatusType.BUFF, StatusForm.YIN_JI);
            duration(StatusDurationType.WEI_CHI, duration);
            displayNameAndDuration();
            runOn(Trigger.ADDING_CROWD_CONTROL, param ->
                    ((ParamAddCrowdControl) param).getEffectInfo().setCancel(true)
            );
        }
    }

    static class StatusIncreaseAttack extends Status {
        public StatusIncreaseAttack(Character from, Character belongTo, int duration, double ratio) {
            super(SkillName + "攻击", from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);

            duration(StatusDurationType.WEI_CHI, duration);
            attribute(Attribute.ATTACK, _ -> belongTo.getInitBaseAttack() * ratio);
        }
    }
}
