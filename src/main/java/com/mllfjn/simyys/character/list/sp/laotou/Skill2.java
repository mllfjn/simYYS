package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventRoundDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.List;
import java.util.Optional;

class Skill2 extends PassiveSkill {
    public static final String SkillName = "洪福降临";

    private final StatusAfterRound status;

    public Skill2(LaoTou laoTou, int level) {
        super(laoTou, level, 2);
        this.status = new StatusAfterRound(laoTou, level);
        // lv5-战斗开始后,自身首次受到伤害时开始打盹
        if (level >= 5) {
            laoTou.addStatus(new StatusFirstAttackListener(laoTou, level));
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void enable() {
        getBelongTo().addStatus(status);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(status);
    }

    static class StatusAfterRound extends Status implements StatusRunnable {
        private final int level;

        public StatusAfterRound(LaoTou laoTou, int level) {
            super(laoTou, laoTou, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.level = level;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            // 自身回合结束时，击退全体敌方目标10%行动条
            // lv2-击退行动条效果提升至15%
            belongTo.doInteractive(interactive -> {
                List<Character> enemy = new CharacterFinder(belongTo)
                        .filterEnemy()
                        .getList();
                for (Character character : enemy) {
                    interactive.decreaseLocation(character, level >= 2 ? 15 : 10);
                }
            });

            // 若回合中释放过委以重任,则开始打盹
            if (belongTo.isHaveStatus(StatusUse3Flag.class)) {
                belongTo.addStatus(new StatusDaDun(belongTo, level >= 4));
                // lv4-打盹额外获得1点鬼火
                if (level >= 4) {
                    belongTo.bp.gainGuiHuo(belongTo, 1);
                }
            }

            // lv3-回合结束时额外获得2点鬼火
            if (level >= 3) {
                belongTo.bp.gainGuiHuo(belongTo, 2);
            }

            return false;
        }
    }

    static class StatusFirstAttackListener extends Status implements StatusRunnable {
        private final boolean levelGZ4;

        public StatusFirstAttackListener(LaoTou laoTou, int level) {
            super(laoTou, laoTou, StatusType.SPECIAL, StatusForm.SPECIAL);
            levelGZ4 = (level >= 4);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            belongTo.bp.addActionListener(belongTo, event -> {
                if (event instanceof EventRoundDone) {
                    belongTo.addStatus(new StatusDaDun(belongTo, levelGZ4));
                    return true;
                }
                return false;
            });
            return true;
        }
    }
}


