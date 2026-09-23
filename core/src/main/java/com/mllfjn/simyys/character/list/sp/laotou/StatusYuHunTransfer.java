package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.EquipFactory;

public class StatusYuHunTransfer extends Status {
    private final Equip addedEquip;

    private boolean transfer = true;

    private StatusYuHunBeingTransfer beingTransfer;

    public StatusYuHunTransfer(Character from, Character belongTo, Equip newEquip) {
        super("转移御魂", from, belongTo);
        duration(StatusDurationType.WEI_CHI, 1);

        for (Equip equip : belongTo.getYuHunSet()) {
            if (equip.getClass() == newEquip.getClass()) {
                transfer = false;
                break;
            }
        }

        if (transfer) {
            addedEquip = EquipFactory.getEquip(newEquip.getName(), belongTo, false).orElseThrow();
            belongTo.addYuHun(addedEquip);
            beingTransfer = new StatusYuHunBeingTransfer(belongTo, ((LaoTou) from), newEquip, this);
            from.addStatus(beingTransfer);
        } else {
            addedEquip = null;
        }

        beforeDelete(() -> {
            if (transfer && addedEquip != null) {
                belongTo.removeYuHun(addedEquip);
                beingTransfer.delete();
            }
        });
        displayName();
    }
}
