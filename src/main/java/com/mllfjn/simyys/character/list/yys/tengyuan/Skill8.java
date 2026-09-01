package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;

import java.util.List;

class Skill8 extends PassiveSkill {
    static final String SkillName = "余音入梦";

    public Skill8(TengYuanDaoZhang belongTo, int level, int shuYin) {
        super(belongTo, level, 8);
        belongTo.addStatus(new StatusPreventDie(belongTo, level >= 6 ? 6 : level - 1, shuYin));
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t受到致命伤害后,移除自身所有减益并化为梦境意识留存于场上,持续回合数等同于当前场上存活友方式神数
                √\tlv2-5:触发时获得(lv-1)层律音
                √\tlv6-触发时获得6层律音
                √\t梦境意识:免疫伤害,控制效果
                \t\t与放逐.当场上仅剩梦境意识时,战斗失败
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    class StatusPreventDie extends Status {

        public StatusPreventDie(TengYuanDaoZhang character, int getLvYin, int shuYin) {
            super(SkillName, character);
            preventDie(_ -> {
                belongTo.removeAllDeBuff();
                belongTo.addStatus(new StatusDeadLine(belongTo));
                if (getLvYin > 0) {
                    character.getLvYin().addStack(getLvYin);
                }
                if (shuYin > 0) {
                    List<Character> list = new CharacterFinder(belongTo)
                            .filterTeammate()
                            .getList();
                    belongTo.doInteractive(interactive ->
                            interactive.healTypical(Skill8.this, list, 10 * shuYin)
                    );
                }
                delete();
            });
            displayName();
        }

        static class StatusDeadLine extends Status {
            private static final String StatusName = "梦境意识";

            public StatusDeadLine(Character character) {
                super(StatusName, character);
                int duration = new CharacterFinder(character, true)
                        .filterTeammate()
                        .filterShiShen()
                        .getCount();
                duration(StatusDurationType.CHI_XU, duration);
                runOn(Trigger.ADDING_CROWD_CONTROL, param ->
                        ((ParamAddCrowdControl) param).getEffectInfo().setCancel(true)
                );
                runOn(Trigger.BEING_ATTACKED, param ->
                        ((ParamAttackInfo) param).getAttackInfo().setCancel(true)
                );
                beforeDelete(() -> belongTo().die());
                displayNameAndDuration();
                preventDie();
            }
        }
    }
}
