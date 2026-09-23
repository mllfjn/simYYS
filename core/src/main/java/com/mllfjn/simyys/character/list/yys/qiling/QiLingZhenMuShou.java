package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;

import java.util.List;

class QiLingZhenMuShou {
    static final String QiLingName = "镇墓兽";

    static void install(Character character) {
        Status status = Status.of(QiLingName + "血量监听", character);
        status.runOn(Trigger.HP_CHANGE, _ -> {
            if (character.getHpPercent() < 0.5) {
                QiLingFactory.yuHunEffect(character, QiLingName);
                List<Character> list = new CharacterFinder(character)
                        .filterTeammate()
                        .getList();
                for (Character target : list) {
                    Status.of("契镇", character, target)
                            .type(StatusType.BUFF, StatusForm.ZHUANG_TAI)
                            .attribute(Attribute.CRIT_POWER, 30)
                            .displayName()
                            .addTo();
                }
                status.delete();
            }
        }).addTo();
    }
}
