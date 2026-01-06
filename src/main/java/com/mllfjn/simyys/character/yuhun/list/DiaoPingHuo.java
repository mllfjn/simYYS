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
import com.mllfjn.simyys.interactive.InteractiveInfo;

import java.util.List;

public class DiaoPingHuo extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "钓瓶火";

    private StatusDPHAfterRound status;

    @Override
    public void init(Character character) {
        super.init(character);

        status = new StatusDPHAfterRound(character);
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
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
            belongTo.doInteractive(interactive -> interactive.heal(skill, List.of(target)
                    , (c) -> InteractiveInfo.createHeal(belongTo, skill, c
                            , (c1, c2) -> belongTo.getDefence() * 7)));
            return false;
        }
    }
}
