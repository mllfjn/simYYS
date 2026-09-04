package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class ZhenMuShou extends Equip implements YuHunSealResponse {
    public static final String YuHunName = "镇墓兽";

    private Status status;

    private double percentage;
    private boolean counting;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        status = Status.of(YuHunName, character)
                .runOn(Trigger.HP_CHANGE, _ -> {
                    ZhenMuShou.this.yuHunEffect();
                    percentage = (100 - Math.ceil(100 * character.getHpPercent())) * 0.005;

                })
                .attribute(Attribute.CRIT_POWER, _ -> {
                    if (counting) {
                        return 0.0;
                    }

                    counting = true;
                    double rt = character.getCritPower() * percentage;
                    counting = false;
                    return rt;
                });
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
