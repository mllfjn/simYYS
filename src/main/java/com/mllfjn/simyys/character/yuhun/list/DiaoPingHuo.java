package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.interactive.HealInfo;

import java.util.List;

public class DiaoPingHuo extends Equip implements YuHunSealResponse {
    public static final String YuHunName = "钓瓶火";

    private StatusDPHAfterRound status;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);

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

    static class StatusDPHAfterRound extends Status {
        private final Skill skill = Skill.getInstance(YuHunName);

        public StatusDPHAfterRound(Character character) {
            super(YuHunName + "回合后监听", character);
            runOn(Trigger.AFTER_ROUND, _ -> {
                character.bp().addGuiHuoProgress(belongTo.team);
                // 为当前（生命比例最低）的（非召唤物）（友方）目标治疗自身防御700%的生命
                Character target = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .filterSummon(false)
                        .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
                HealInfo healInfo = HealInfo.createHeal(belongTo, skill, target, belongTo.getDefence());
                healInfo.setMultiplier(700);
                belongTo.doInteractive(interactive -> interactive.heal(skill, List.of(target)
                        , _ -> healInfo));
            });
        }
    }
}
