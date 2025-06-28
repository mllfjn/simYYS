package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class Skill implements Serializable {
    public String name;
    private final Character belongTo;
    private final int level;
    public String lastUsedTarget;
    private final int cost;
    private final int coolDown;
    private int cooling;

    public Skill(Character belongTo, int level, int cost, int coolDown) {
        this.belongTo = belongTo;
        this.level = level;
        this.cost = cost;
        this.coolDown = coolDown;
        setName();
    }

    public abstract void setName();
    public void use(BattlePane bp) {
        usePrivate(bp);
        cooling = coolDown;
        StringBuilder sb = new StringBuilder(belongTo.name);
        if (lastUsedTarget != null) {
            sb.append("对").append(lastUsedTarget);
        }
        sb.append("使用了").append(name);
        bp.log.addText(sb.toString());
    }
    public abstract void usePrivate(BattlePane bp);
    public boolean canUse(BattlePane bp) {
        return cooling == 0 && bp.canUseGuiHuo(belongTo, cost);
    }

    public Character getBelongTo() {
        return this.belongTo;
    }

    public int getLevel() {
        return this.level;
    }

}
