package com.mllfjn.simyys.character.list.ssr.xunxiangxing;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.StatusSupplier;

class StatusFuHunXiang extends Status implements Displayable, AttributeModifier {
    static final String StatusName = "缚魂香";

    private final double percent;
    private final Skill2 skill2;
    private int defenseStack = 1;
    private int damageStack = 1;

    public StatusFuHunXiang(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        XunXiangXing xxx = (XunXiangXing) from;
        skill2 = xxx.getSkill2();
        percent = (xxx).getPercent();
    }

    static StatusSupplier getSupplier() {
        return new StatusSupplier(StatusName, StatusFuHunXiang.class, (from, to) -> {
            to.getStatus(StatusFuHunXiang.class)
                    .ifPresentOrElse(
                            StatusFuHunXiang::addStack,
                            () -> to.addStatus(new StatusFuHunXiang(from, to))
                    );
        });
    }

    private void addStack() {
        if (defenseStack < 5) {
            defenseStack++;
        }

        if (damageStack == 2) {
            damageStack = 0;
            StatusShiShen.install(from, belongTo);
            AttackInfo.createJianJieAttack(from, skill2, belongTo, (c1, c2) ->)
            from.bp.interactive.attack();
        } else {
            damageStack++;
        }
    }

    @Override
    public String getDisplayText() {
        StringBuilder sb = new StringBuilder("减防");
        sb.append(defenseStack);
        if (damageStack > 0) {
            sb.append(Displayable.DELIMITER).append(StatusName).append(damageStack);
        }
        return sb.toString();
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return defenseStack * percent * belongTo.getInitDefense();
    }

}
