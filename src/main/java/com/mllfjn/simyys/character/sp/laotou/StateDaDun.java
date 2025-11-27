package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.state.StateSettleType;
import com.mllfjn.simyys.state.instance.StateSleep;

import java.util.List;

public class StateDaDun extends StateSleep {
    public StateDaDun(LaoTou laoTou) {
        super(laoTou, laoTou);
        setSettleType(StateSettleType.WEI_CHI, 1);
    }

    @Override
    public void beforeDelete() {
        belongTo.doInteractive(interactive -> {
            // 沉睡维持期间被移除时，恢复全体非召唤物友方目标生命上限14%的生命
            List<Character> teammate = CharacterFinder.findTeammateExceptSummon(belongTo, belongTo.bp.situation.characters);
            for (Character character : teammate) {
                interactive.recovery(character, belongTo.getMaxHp() * 0.14);
            }
        });
    }
}
