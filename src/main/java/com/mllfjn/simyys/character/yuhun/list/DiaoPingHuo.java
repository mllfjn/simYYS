package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class DiaoPingHuo extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "钓瓶火";

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    static class StatusDPHAfterRound extends Status implements StatusRunnable {
        private final Skill skill = Skill.getInstance(YuHunName);

        public StatusDPHAfterRound(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            bp.addGuiHuoProgress(belongTo.team);
            // 为当前（生命比例最低）的（非召唤物）（友方）目标治疗自身防御700%的生命
            Character target = new CharacterFinder(belongTo)
                    .setTargetTeam(CharacterFinder.TargetTeam.TEAMMATE)
                    .filterSummon(false)
                    .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
            belongTo.doInteractive(interactive -> interactive.healTypical(skill, target, ));
            return false;
        }
    }
}
