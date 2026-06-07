package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunAfterCauseAttack;
import com.mllfjn.simyys.character.yuhun.YuHunUnfullMark;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

public class GuiLingGeJi extends YuHun implements YuHunUnfullMark, YuHunAfterCauseAttack {
    public static final String YuHunName = "鬼灵歌姬";
    private static final Skill skill = Skill.getInstance(YuHunName);

    private StatusCountRecord status;

    @Override
    public String getName() {
        return YuHunName;
    }

    private StatusCountRecord record() {
        if (status == null) {
            status = new StatusCountRecord(character);
            character.addStatus(status);
        }
        return status;
    }

    @Override
    public void action(AttackInfo attackInfo, Interactive interactive) {
        if (record().count < 5) {
            record().count++;
        } else {
            // 设为-1以抵消这次鬼灵歌姬伤害触发
            record().count = -1;
            Character target = attackInfo.getTarget();
            AttackInfo aInfo = AttackInfo.createRealAttack(character, skill, target,
                    Math.min(target.getMaxHp() * 0.2, character.getAttack() * 2.55)
            );
            interactive.attack(aInfo);
            yuHunEffect();
        }
    }

    private class StatusCountRecord extends Status {
        private int count = 0;

        public StatusCountRecord(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void beforeDelete() {
            GuiLingGeJi.this.status = null;
        }
    }
}
