package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAfterCauseAttack;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

public class ZhenNv extends YuHun implements YuHunAfterCauseAttack {
    public static final String YuHunName = "针女";
    private static final Skill skill = Skill.getInstance(YuHunName);

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void action(AttackInfo attackInfo, Interactive interactive) {
        if (attackInfo.isCrit()) {
            if (RateController.yuHun(character, this, 40)) {
                AttackInfo info = AttackInfo.createRealAttack(character, skill, attackInfo.getTarget(),
                        attackInfo.getTarget().getMaxHp());
                info.setMultiplier(10);
                info.setLimit(character.getAttack() * 1.2);
                interactive.attack(info);
            }
        }
    }
}
