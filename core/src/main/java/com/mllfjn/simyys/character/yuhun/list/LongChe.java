package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunHitFeedBack;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.ratecontroller.RateController;

public class LongChe extends Equip implements YuHunUnfullMark, YuHunHitFeedBack {
    public static final String YuHunName = "胧车";

    private boolean triggered = false;

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void hitFeedBack(AttackInfo info) {
        if (!triggered) {
            if (info.getAttacker().isMob()) {
                if (RateController.yuHun(character, LongChe.this, 50)) {
                    triggered = true;
                    Skill causeSkill = info.getSkill();
                    LongChe.this.yuHunEffect();
                    character.doInteractive(interactive ->
                            interactive.increaseLocation(character, 30)
                    );
                    causeSkill.addSkillEndListener(() -> triggered = false);
                }
            }
        }
    }
}
