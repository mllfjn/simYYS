package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.yuhun.Equip;

public class TianYuYuZhan extends Equip {
    public static final String EQUIP_NAME = "天羽羽斩";

    @Override
    public String getName() {
        return EQUIP_NAME;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        if (character.bp().isMobBattle(character)) {
            Status.of(EQUIP_NAME, character)
                    .attribute(Attribute.IGNORE_DEFENCE, 50)
                    .retainAfterDie()
                    .retainAfterChangeWave()
                    .addTo();
        }
    }
}
