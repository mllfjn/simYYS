package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.instance.StatusSleep;

import java.util.List;

public class StatusDaDun extends StatusSleep {
    public StatusDaDun(LaoTou laoTou) {
        super(laoTou, laoTou);
        setDurationType(StatusDurationType.WEI_CHI, 1);
    }

    @Override
    public void beforeDelete() {
        if (belongTo.bp.situation.characterActing != belongTo) {
            belongTo.doInteractive(interactive -> {
                // 沉睡维持期间被移除时，恢复全体非召唤物友方目标生命上限14%的生命
                List<Character> teammate = new CharacterFinder(belongTo)
                        .setTargetTeam(CharacterFinder.TargetTeam.TEAMMATE)
                        .filterSummon(false)
                        .getList();
                for (Character character : teammate) {
                    interactive.recovery(character, belongTo.getMaxHp() * 0.14);
                }
            });
        }
    }
}
