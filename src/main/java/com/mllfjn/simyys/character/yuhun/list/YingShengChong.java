package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class YingShengChong extends Equip implements YuHunSealResponse {
    public static final String YuHunName = "应声虫";

    private Status status;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = Status.of(YuHunName, character)
                .attribute(Attribute.XIE_ZHAN, 20)
                .retainAfterDie()
                .retainAfterChangeWave();
    }

    @Override
    public void enable() {
        character.addStatus(status);
    }

    @Override
    public void disable() {
        character.removeStatus(status);
    }
}
