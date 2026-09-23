package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.instance.StatusSleep;

import java.util.List;

public class StatusDaDun extends StatusSleep {
    private static final String StatusName = "打盹";

    public StatusDaDun(Character laoTou, boolean levelGZ4) {
        super(laoTou, laoTou);
        // lv4-打盹额外获得1点鬼火
        if (levelGZ4) {
            laoTou.bp.gainGuiHuo(laoTou, 1);
        }
        duration(StatusDurationType.WEI_CHI, 1);
        displayName();
        runOn(Trigger.AFTER_ATTACK, _ -> {
            // 沉睡维持期间被移除时，恢复全体非召唤物友方目标生命上限14%的生命
            belongTo.doInteractive(interactive -> {
                List<Character> teammate = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .filterSummon(false)
                        .getList();
                for (Character character : teammate) {
                    interactive.recovery(Skill.getInstance(StatusName), character, belongTo.getMaxHp() * 0.14);
                }
            });
        });
    }
}
