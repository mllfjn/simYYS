package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.InteractiveInfo;
import com.mllfjn.simyys.character.status.instance.StatusShield;

class StatusShenWuHuDun extends StatusShield {
    private static final String StatusName = "蜃气护盾";

    private int count = 3;

    private StatusShenWuHuDun(Character character) {
        // 获得最大生命值100%蜃雾护盾
        super(character, character, character.getMaxHp());
        display(() -> StatusName + count);
    }

    public static void get(Character character) {
        character.getStatus(StatusShenWuHuDun.class).ifPresentOrElse(
                status -> {
                    status.setShield(character.getMaxHp());
                    status.count = 3;
                }
                , () -> character.addStatus(new StatusShenWuHuDun(character)));
    }

    @Override
    public boolean handle(InteractiveInfo interactiveInfo) {
        if (super.handle(interactiveInfo)) {
            return true;
        }
        // 使用暴击伤害攻击蜃气楼可击破护盾,抵挡3次暴击伤害后，护盾消失。
        if (interactiveInfo.isCrit()) {
            count--;
        }
        return count == 0;
    }
}
