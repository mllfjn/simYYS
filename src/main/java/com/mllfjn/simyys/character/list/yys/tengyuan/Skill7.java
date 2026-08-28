package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

class Skill7 extends PassiveSkill {
    static final String SkillName = "调律润心";

    private final int shuYin;

    public Skill7(TengYuanDaoZhang belongTo, int shuYin) {
        super(belongTo, 1, 7);
        this.shuYin = shuYin;
        belongTo.getLvYin().setSkill7(this);
        belongTo.bp.addStatusAdder(c ->
                c.team != belongTo.team && c.isShiShen()
                        ? new StatusUseSkillListener(belongTo, c)
                        : null
        );
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t每消耗1层律音,提升自身以及行动条最前端的友方式神5%行动条
                √\t敌方式神释放妖术时,提升自身10%行动条
                \t\t术印到底是33%还是33.33%没测，按33.33%写的
                """;
    }

    void useLvYin(int count) {
        Character belongTo = getBelongTo();
        final double increase = 5 * count;
        if (shuYin >= 3
                || RateController.otherWhether(SkillName, "提升行动条效果作用于全体友方", belongTo.bp().calc,
                shuYin * 100.0 / 3)
        ) {
            List<Character> list = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .getList();
            belongTo.doInteractive(interactive -> {
                for (Character character : list) {
                    interactive.increaseLocation(character, increase);
                }
            });
        } else {
            Character locationMax = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .get(Attribute.LOCATION, CharacterFinder.Criteria.MAX);

            belongTo.doInteractive(interactive -> {
                interactive.increaseLocation(belongTo, increase);
                interactive.increaseLocation(locationMax, increase);
            });
        }
    }

    @Override
    public String getName() {
        return SkillName;
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
            from.doInteractive(interactive -> interactive.increaseLocation(from, 10));
            return false;
        }
    }
}
