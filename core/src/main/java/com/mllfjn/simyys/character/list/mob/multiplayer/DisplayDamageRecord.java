package com.mllfjn.simyys.character.list.mob.multiplayer;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

public class DisplayDamageRecord extends StatusRecordDamage implements InfoDisplay {
    private double totalDamage;

    public DisplayDamageRecord(Character character) {
        super(character);
    }

    @Override
    public void addDamage(double damage) {
        this.totalDamage += damage;
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder("我的伤害:");
        if (totalDamage > 10000) {
            sb.append(String.format("%.1f", totalDamage / 10000)).append("万");
        } else {
            sb.append("<1万");
        }
        return sb.toString();
    }
}
