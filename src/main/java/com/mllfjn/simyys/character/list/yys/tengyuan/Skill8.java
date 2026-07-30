package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.PreventDie;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.List;

class Skill8 extends PassiveSkill {
    static final String SkillName = "余音入梦";

    public Skill8(Character belongTo, int level, int shuYin) {
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

    class StatusPreventDie extends Status implements PreventDie {
        private final int getLvYin;
        private final int shuYin;

        public StatusPreventDie(Character character, int getLvYin, int shuYin) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.getLvYin = getLvYin;
            this.shuYin = shuYin;
        }

        @Override
        public void preventDie(double excessDamage) {
            belongTo.removeAllDeBuff();
            belongTo.addStatus(new StatusDeadLine(belongTo));
            if (getLvYin > 0) {
                TengYuanDaoZhang.getLvYin(belongTo).addStack(getLvYin);
            }
            if (shuYin > 0) {
                List<Character> list = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .getList();
                belongTo.doInteractive(interactive -> {
                    interactive.healTypical(Skill8.this, list, 10 * shuYin);
                });
            }
            delete();
        }

        @Override
        public String getName() {
            return SkillName;
        }

        static class StatusDeadLine extends Status implements StatusRunnable, Displayable, PreventDie {
            private static final String StatusName = "梦境意识";

            public StatusDeadLine(Character character) {
                super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
                int duration = new CharacterFinder(character, true)
                        .filterTeammate()
                        .filterShiShen()
                        .getCount();
                setDurationType(StatusDurationType.CHI_XU, duration);
            }

            @Override
            public void beforeDelete() {
                belongTo.die();
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return trigger == Trigger.ADDING_CROWD_CONTROL || trigger == Trigger.BEING_ATTACKED;
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                if (param instanceof ParamAddCrowdControl pac) {
                    pac.getEffectInfo().setCancel(true);
                } else {
                    ((ParamAttackInfo) param).getAttackInfo().setCancel(true);
                }
                return false;
            }

            @Override
            public String getDisplayText() {
                return StatusName + getDuration();
            }

            @Override
            public void preventDie(double excessDamage) {

            }

            @Override
            public String getName() {
                return StatusName;
            }
        }
    }
}
